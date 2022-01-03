package com.programming.techie.rapiddeploy.model;

import com.github.dockerjava.api.model.MountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class DockerContainerPayload {
    private String imageId;
    private List<EnvironmentVariables> environmentVariables;
    private String name;
    private Integer port;
    private Integer exposedPort;
    private List<Volume> volumeList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Volume {
        private String mountSource;
        private String mountTarget;
        private MountType mountType;
    }
}
