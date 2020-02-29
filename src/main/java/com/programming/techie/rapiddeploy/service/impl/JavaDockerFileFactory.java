package com.programming.techie.rapiddeploy.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.service.DockerfileFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static java.nio.file.Files.find;

@Service
public class JavaDockerFileFactory implements DockerfileFactory {

    private ImmutableList<String> rootFileList = ImmutableList.of("pom.xml", "build.gradle");
    private ImmutableMap<String, String> buildCommandMap = ImmutableMap.<String, String>builder()
            .put("pom.xml", "mvn -B -DskipTests=true clean install")
            .put("build.gradle", "")
            .build();

    @Override
    public String createDockerFileContent(Path extractedFilePath, String baseImage) {
        String rootFile = rootFileList.stream()
                .filter(file -> findRootFile(extractedFilePath, file))
                .collect(onlyElement());
        String buildCommand = buildCommandMap.get(rootFile);
        return null;
    }

    @Override
    public File createDockerFile(String content) {
        return null;
    }

    public boolean findRootFile(Path filePath, String rootFileName) {
        try {
            return find(filePath, 1,
                    ((path, basicFileAttributes) -> path.getFileName().toString()
                            .equals(rootFileName)))
                    .findAny()
                    .isPresent();
        } catch (IOException e) {
            throw new RapidDeployException("Exception occurred when finding " + rootFileName + " file");
        }
    }
}
