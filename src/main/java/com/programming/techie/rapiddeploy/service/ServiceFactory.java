package com.programming.techie.rapiddeploy.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.google.common.base.Preconditions;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.ManagedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ServiceFactory {

    public void pullImage(ManagedService managedService) {
        DockerClient client = DockerClientManager.getClient();
        try {
            client.pullImageCmd(managedService.getServiceTemplate().getImageName())
                    .withTag(managedService.getServiceTemplate().getTagName())
                    .exec(new PullImageResultCallback())
                    .awaitCompletion();
        } catch (Exception e) {
            throw new RapidDeployException("An exception occurred while pulling docker image for - " + managedService.getServiceTemplate().getImageName(), e);
        }
    }

    public String startManagedServiceContainer(ManagedService managedService) {
        List<String> environmentVariables = managedService.getEnvironmentVariables()
                .stream()
                .map(env -> env.getKey() + "=" + env.getValue())
                .collect(toList());

        DockerClient dockerClient = DockerClientManager.getClient();
        String imageId = dockerClient.inspectImageCmd(managedService.getServiceTemplate().getImageName()).getImageId();
        Preconditions.checkArgument(imageId != null);
        CreateContainerResponse container = dockerClient.createContainerCmd(imageId)
                .withEnv(environmentVariables)
                .exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    public void stopManagedServiceContainer(ManagedService managedService) {
        DockerClient dockerClient = DockerClientManager.getClient();
        String imageId = dockerClient.inspectImageCmd(managedService.getServiceTemplate().getImageName()).getImageId();
        Preconditions.checkArgument(imageId != null);
        dockerClient.stopContainerCmd(managedService.getContainerId()).withContainerId(managedService.getContainerId()).exec();
    }
}
