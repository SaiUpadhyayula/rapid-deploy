package com.programming.techie.rapiddeploy.service.docker.impl;

import com.programming.techie.rapiddeploy.model.ManifestDefinition;
import com.programming.techie.rapiddeploy.service.docker.DockerfileFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class PhpDockerfileFactory implements DockerfileFactory {
    @Override
    public String createDockerFileContent(Path extractedFilePath, ManifestDefinition manifestDefinition) {
        return null;
    }
}
