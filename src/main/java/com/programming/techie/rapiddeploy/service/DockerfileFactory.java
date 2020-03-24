package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.model.ManifestDefinition;

import java.nio.file.Path;

public interface DockerfileFactory {
    String createDockerFileContent(Path extractedFilePath, ManifestDefinition manifestDefinition);
}
