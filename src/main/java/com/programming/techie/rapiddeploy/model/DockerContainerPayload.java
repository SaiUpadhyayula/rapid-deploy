package com.programming.techie.rapiddeploy.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DockerContainerPayload {
    private String imageId;
    private List<EnvironmentVariables> environmentVariables;
    private String name;
    private Integer port;
    private Integer exposedPort;
}
