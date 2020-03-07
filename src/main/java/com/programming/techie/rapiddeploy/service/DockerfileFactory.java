package com.programming.techie.rapiddeploy.service;

import java.nio.file.Path;

public interface DockerfileFactory {
    String createDockerFileContent(Path extractedFilePath, String baseImage);
}
