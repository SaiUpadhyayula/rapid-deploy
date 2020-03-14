package com.programming.techie.rapiddeploy.service.impl.dockerfile;

import com.programming.techie.rapiddeploy.model.ManifestDefinition;
import com.programming.techie.rapiddeploy.service.DockerfileFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class PhpDockerfileFactory implements DockerfileFactory {
    @Override
    public String createDockerFileContent(Path extractedFilePath, String baseImage, ManifestDefinition manifestDefinition) {
        return null;
    }
}
