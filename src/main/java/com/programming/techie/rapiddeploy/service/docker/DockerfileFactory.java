package com.programming.techie.rapiddeploy.service.docker;

import com.programming.techie.rapiddeploy.model.ManifestDefinition;

import java.nio.file.Path;

public interface DockerfileFactory {
    default String createDockerFileContent(Path extractedFilePath, ManifestDefinition manifestDefinition) {
        String dockerFile =
                "FROM gliderlabs/herokuish:latest" + System.lineSeparator() +
                        "COPY . /app" + System.lineSeparator() +
                        "EXPOSE %s" + System.lineSeparator() +
                        "CMD /build && /start web";
        return String.format(dockerFile, 8080);
    }
}
