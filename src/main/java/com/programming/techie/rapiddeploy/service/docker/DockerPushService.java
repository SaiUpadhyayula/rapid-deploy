package com.programming.techie.rapiddeploy.service.docker;

import com.github.dockerjava.api.DockerClient;

public class DockerPushService {

    public void push(String imageId) {
        DockerClient dockerClient = DockerClientManager.getClient();

//        dockerClient.tagImageCmd(imageId, )
    }
}
