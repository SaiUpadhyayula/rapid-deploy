package com.programming.techie.rapiddeploy.service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.google.common.base.Preconditions;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class DockerImageService {

    public String buildDockerImage(File dockerFile) {
        log.info("Building Docker Image..");
        DockerClient dockerClient = DockerClientManager.getClient();
        String imageId = dockerClient.buildImageCmd()
                .withTags(Collections.singleton("latest"))
                .withDockerfile(dockerFile)
                .withPull(true)
                .withNoCache(true)
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        log.info("Completed Building Docker Image - {}", imageId);
        return imageId;
    }

    public void pullImage(String imageName, String tagName) {
        DockerClient client = DockerClientManager.getClient();
        try {
            client.inspectImageCmd(getImageId(imageName)).exec();
        } catch (Exception e) {
            try {
                client.pullImageCmd(imageName)
                        .withTag(tagName)
                        .exec(new PullImageResultCallback())
                        .awaitCompletion();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new RapidDeployException("An exception occurred while pulling docker image for - " + imageName, e);
            }
        }
    }

    public String getImageId(String imageName) {
        DockerClient dockerClient = DockerClientManager.getClient();
        String imageId = dockerClient.inspectImageCmd(imageName).getImageId();
        Preconditions.checkArgument(imageId != null);
        return imageId;
    }
}
