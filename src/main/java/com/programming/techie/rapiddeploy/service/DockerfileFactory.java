package com.programming.techie.rapiddeploy.service;

import java.io.File;
import java.nio.file.Path;

public interface DockerfileFactory {
    String createDockerFileContent(Path extractedFilePath, String baseImage);

    File createDockerFile(String content);
}
