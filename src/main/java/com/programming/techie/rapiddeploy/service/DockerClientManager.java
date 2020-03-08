package com.programming.techie.rapiddeploy.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DockerClientManager {

    public static DockerClient getClient() {
        return DockerClientBuilder.getInstance("tcp://localhost:2375").build();
    }
}
