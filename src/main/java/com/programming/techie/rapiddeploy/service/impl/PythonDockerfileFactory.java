package com.programming.techie.rapiddeploy.service.impl;

import com.programming.techie.rapiddeploy.service.DockerfileFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public class PythonDockerfileFactory implements DockerfileFactory {
    @Override
    public String createDockerFileContent(Path extractedFilePath, String baseImage) {
        return null;
    }
}
