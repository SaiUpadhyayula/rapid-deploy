package com.programming.techie.rapiddeploy.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.google.common.base.Preconditions;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.NETWORK_NAME;
import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.RAPID_DEPLOY_SERVICE_PREFIX;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

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

    public String buildDockerImage(File dockerFile) {
        log.info("Building Docker Image..");
        DockerClient dockerClient = DockerClientManager.getClient();
        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(dockerFile)
                .withPull(true)
                .withNoCache(true)
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        log.info("Completed Building Docker Image - {}", imageId);
        return imageId;
    }

    public Pair<String, String> run(DockerContainerPayload dockerContainerPayload) {
        List<String> envList = dockerContainerPayload.getEnvironmentVariables()
                .stream()
                .map(env -> env.getKey() + "=" + env.getValue())
                .collect(toList());
        String containerName = RAPID_DEPLOY_SERVICE_PREFIX + dockerContainerPayload.getName();
        DockerClient dockerClient = DockerClientManager.getClient();

        ExposedPort http8080 = ExposedPort.tcp(dockerContainerPayload.getPort());

        Ports portBindings = new Ports();
        portBindings.bind(http8080, Ports.Binding.bindPort(dockerContainerPayload.getExposedPort()));

        CreateContainerResponse container = dockerClient.createContainerCmd(dockerContainerPayload.getImageId())
                .withEnv(envList)
                .withName(RAPID_DEPLOY_SERVICE_PREFIX + dockerContainerPayload.getName())
                .withExposedPorts(http8080)
                .withHostConfig(HostConfig.newHostConfig()
                        .withNetworkMode(NETWORK_NAME)
                        .withAutoRemove(true)
                        .withPortBindings(portBindings))
                .exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        log.info("Container ID - {}", container.getId());
        return Pair.of(container.getId(), containerName);
    }

    public void stop(String containerId) {
        log.info("Stopping container with id {}", containerId);
        DockerClient dockerClient = DockerClientManager.getClient();
        dockerClient.stopContainerCmd(containerId).withContainerId(containerId).exec();
        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerId).exec();
        Preconditions.checkArgument(Objects.equals(containerResponse.getState().getRunning(), false));
    }

    public void pullImage(String imageName, String tagName) {
        DockerClient client = DockerClientManager.getClient();
        try {
            client.pullImageCmd(imageName)
                    .withTag(tagName)
                    .exec(new PullImageResultCallback())
                    .awaitCompletion();
        } catch (Exception e) {
            throw new RapidDeployException("An exception occurred while pulling docker image for - " + imageName, e);
        }
    }

    public String getImageId(String imageName) {
        DockerClient dockerClient = DockerClientManager.getClient();
        String imageId = dockerClient.inspectImageCmd(imageName).getImageId();
        Preconditions.checkArgument(imageId != null);
        return imageId;
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
}
