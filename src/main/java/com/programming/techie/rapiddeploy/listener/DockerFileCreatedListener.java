package com.programming.techie.rapiddeploy.listener;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.programming.techie.rapiddeploy.events.DockerfileCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DockerFileCreatedListener {

    @EventListener
    public void handle(DockerfileCreated dockerfileCreated) {
        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://localhost:2375").build();
        log.info("Building Docker Image..");
        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(dockerfileCreated.getFile())
                .withPull(true)
                .withNoCache(true)
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        log.info("Completed Building Docker Image.");
        List<Container> containers = dockerClient.listContainersCmd()
                .withShowSize(true)
                .withShowAll(true)
                .withStatusFilter(Collections.singleton("exited")).exec();
        for (Container container : containers) {
            log.info(container.getImageId());
        }

        CreateContainerResponse container = dockerClient.createContainerCmd(imageId).exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        log.info("Container ID - " + container.getId());
    }
}
