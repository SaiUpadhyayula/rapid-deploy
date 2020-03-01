package com.programming.techie.rapiddeploy.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.service.DockerfileFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.programming.techie.rapiddeploy.util.FileUtils.BUILD_GRADLE;
import static com.programming.techie.rapiddeploy.util.FileUtils.POM_XML;
import static java.nio.file.Files.find;

@Service
public class JavaDockerFileFactory implements DockerfileFactory {

    private ImmutableList<String> rootFileList = ImmutableList.of(POM_XML, BUILD_GRADLE);
    private ImmutableMap<String, String> buildCommandMap = ImmutableMap.<String, String>builder()
            .put(POM_XML, "mvn -B clean install -DskipTests")
            .put(BUILD_GRADLE, "./gradlew stage")
            .build();

    @Override
    public String createDockerFileContent(Path extractedFilePath, String baseImage) {
        String rootFile = rootFileList.stream()
                .filter(file -> findRootFile(extractedFilePath, file))
                .collect(onlyElement());

        if (rootFile.equals(POM_XML) && mavenWrapperDonotExists(extractedFilePath)) {
            copyMavenWrapper();
        }
        return buildCommandMap.get(rootFile);
    }

    private void copyMavenWrapper() {

    }

    private boolean mavenWrapperDonotExists(Path extractedFilePath) {
        return !findRootFile(extractedFilePath, "mvnw.sh");
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
