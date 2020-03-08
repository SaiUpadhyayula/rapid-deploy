package com.programming.techie.rapiddeploy.listener;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.programming.techie.rapiddeploy.events.DockerfileCreated;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.DockerClientManager;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class DockerFileCreatedListener {

    private final ApplicationRepository applicationRepository;

    @SneakyThrows
    @EventListener
    public void handle(DockerfileCreated dockerfileCreated) {
        DockerClient dockerClient = DockerClientManager.getClient();
        log.info("Building Docker Image..");
        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(dockerfileCreated.getFile())
                .withPull(true)
                .withNoCache(true)
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        log.info("Completed Building Docker Image.");

        CreateContainerResponse container = dockerClient.createContainerCmd(imageId).exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        log.info("Container ID - " + container.getId());

        String appGuid = dockerfileCreated.getAppGuid();
        Application application = applicationRepository.findByGuid(appGuid)
                .orElseThrow(() -> new RapidDeployException("No Application found with GUID " + appGuid));
        application.setContainerId(container.getId());
        applicationRepository.save(application);
    }
}
