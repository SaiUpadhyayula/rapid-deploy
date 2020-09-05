package com.programming.techie.rapiddeploy.service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.google.common.base.Preconditions;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.NETWORK_NAME;
import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.RAPID_DEPLOY_SERVICE_PREFIX;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@Slf4j
public class DockerContainerService {

    @PostConstruct
    public void createNetwork() {
        DockerClient client = DockerClientManager.getClient();
        List<Network> networks = client.listNetworksCmd().exec();
        Optional<Network> rapidDeployNetwork = networks.stream().filter(network -> network.getName().equals(NETWORK_NAME)).findAny();
        if (rapidDeployNetwork.isPresent()) {
            log.info("Network - {} already exists", NETWORK_NAME);
        } else {
            client.createNetworkCmd().withName(NETWORK_NAME).exec();
            log.info("Created Network with name - " + NETWORK_NAME);
        }
    }

    public void stop(String containerId) {
        log.info("Stopping container with id {}", containerId);
        DockerClient dockerClient = DockerClientManager.getClient();
        dockerClient.stopContainerCmd(containerId).withContainerId(containerId).exec();
        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerId).exec();
        Preconditions.checkArgument(Objects.equals(containerResponse.getState().getRunning(), false));
    }

    public String inspectContainer(String containerId) {
        DockerClient client = DockerClientManager.getClient();
        StringBuilder stringBuilder = new StringBuilder();
        LogContainerResultCallback callback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                stringBuilder.append(item.toString())
                        .append(System.lineSeparator());
            }
        };
        try {
            client.logContainerCmd(containerId)
                    .withStdErr(true)
                    .withStdOut(true)
                    .withFollowStream(true)
                    .withTailAll()
                    .exec(callback)
                    .awaitCompletion(30, SECONDS);
        } catch (Exception ex) {
            throw new RapidDeployException("Exception Occurred when collecting Build Logs");
        }
        return stringBuilder.toString();
    }

    public Pair<String, String> run(DockerContainerPayload dockerContainerPayload) {
        String containerName = RAPID_DEPLOY_SERVICE_PREFIX + dockerContainerPayload.getName();
        if (checkIfContainerIsRunning(containerName)) {
            log.info("Container is already with the provided name {}, killing the container", containerName);
            killContainer(containerName);
        }
        DockerClient dockerClient = DockerClientManager.getClient();

        Pair<ExposedPort, Ports> portMappings = constructPortMappings(dockerContainerPayload);
        String[] envVarArgs = resolveEnvironmentVariables(dockerContainerPayload);

        CreateContainerResponse container = dockerClient.createContainerCmd(dockerContainerPayload.getImageId())
                .withEnv(envVarArgs)
                .withName(RAPID_DEPLOY_SERVICE_PREFIX + dockerContainerPayload.getName())
                .withExposedPorts(portMappings.getFirst())
                .withHostConfig(HostConfig.newHostConfig()
                        .withNetworkMode(NETWORK_NAME)
                        .withAutoRemove(true)
                        .withMounts(createVolumeMount(dockerContainerPayload))
                        .withPortBindings(portMappings.getSecond()))
                .exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        log.info("Container ID - {}", container.getId());
        log.info("Running container exposed at - {}", portMappings.getSecond());
        inspectContainer(container.getId());
        return Pair.of(container.getId(), containerName);
    }

    public boolean checkIfContainerIsRunning(String containerName) {
        DockerClient client = DockerClientManager.getClient();
        List<Container> containers = client.listContainersCmd()
                .withShowAll(true)
                .exec();
        return containers.stream()
                .anyMatch(container -> asList(container.getNames())
                        .contains("/" + containerName));
    }

    private List<Mount> createVolumeMount(DockerContainerPayload dockerContainerPayload) {
        if (StringUtils.isNotBlank(dockerContainerPayload.getMountSource())
                && StringUtils.isNotBlank(dockerContainerPayload.getMountTarget())) {
            return singletonList(new Mount()
                    .withType(MountType.BIND)
                    .withSource(dockerContainerPayload.getMountSource())
                    .withTarget(dockerContainerPayload.getMountTarget())
                    .withReadOnly(true));
        }
        return Collections.emptyList();
    }

    private Pair<ExposedPort, Ports> constructPortMappings(DockerContainerPayload dockerContainerPayload) {
        ExposedPort http8080 = ExposedPort.tcp(dockerContainerPayload.getPort());
        Ports portBindings = new Ports();
        portBindings.bind(http8080, Ports.Binding.bindPort(dockerContainerPayload.getExposedPort()));
        return Pair.of(http8080, portBindings);
    }

    private String[] resolveEnvironmentVariables(DockerContainerPayload dockerContainerPayload) {
        return dockerContainerPayload.getEnvironmentVariables()
                .stream()
                .map(env -> env.getKey() + "=" + env.getValue()).toArray(String[]::new);
    }


    private void killContainer(String containerName) {
        DockerClient client = DockerClientManager.getClient();
        List<Container> containers = client.listContainersCmd().withShowAll(true).exec();
        Container runningContainer = containers.stream()
                .filter(container -> asList(container.getNames()).contains("/" + containerName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find any container with name - " + containerName));
        client.killContainerCmd(runningContainer.getId()).exec();
    }
}
