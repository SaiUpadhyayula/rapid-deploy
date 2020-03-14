package com.programming.techie.rapiddeploy.service.impl.dockerfile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.ManifestDefinition;
import com.programming.techie.rapiddeploy.service.DockerfileFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.programming.techie.rapiddeploy.util.FileNameUtils.*;
import static java.nio.file.Files.find;

@Service
@Slf4j
public class JavaDockerFileFactory implements DockerfileFactory {

    private ImmutableList<String> rootFileList = ImmutableList.of(POM_XML, BUILD_GRADLE);
    private ImmutableMap<String, String> buildCommandMap = ImmutableMap.<String, String>builder()
            .put(POM_XML, "./mvnw -B clean install -DskipTests")
            .put(BUILD_GRADLE, "./gradlew stage")
            .build();

    @Override
    public String createDockerFileContent(Path extractedFilePath, String baseImage, ManifestDefinition manifestDefinition) {
        String rootFile = rootFileList.stream()
                .filter(file -> findRootFile(extractedFilePath, file))
                .collect(onlyElement());

        if (rootFile.equals(POM_XML) && isMavenWrapperNeeded(extractedFilePath)) {
            copyMavenWrapper(extractedFilePath);
        }

        String buildCommand = buildCommandMap.get(rootFile);
        String dockerFile =
                "FROM %s" + System.lineSeparator() +
                        "RUN mkdir /app" + System.lineSeparator() +
                        "WORKDIR /app" + System.lineSeparator() +
                        "COPY . /app" + System.lineSeparator() +
                        "ENV PORT %s" + System.lineSeparator() +
                        "EXPOSE %s" + System.lineSeparator() +
                        "RUN %s" + System.lineSeparator() +
                        "CMD %s";
        return String.format(dockerFile, baseImage, 8080, 8080, buildCommand, manifestDefinition.getRun());

    }

    @SneakyThrows
    private void copyMavenWrapper(Path extractedFilePath) {
        Path mvnFolderPath = Paths.get(MAVEN_FOLDER).toAbsolutePath().normalize();
        Path mvnWindowsExecutablePath = Paths.get(MAVENW_WINDOWS).toAbsolutePath().normalize();
        Path mvnLinuxExecutablePath = Paths.get(MAVENW_LINUX).toAbsolutePath().normalize();

        // Copy .mvn folder to user project directory
        Path normalize = Paths.get(extractedFilePath.toAbsolutePath().toString() + File.separator + MAVEN_FOLDER).toAbsolutePath().normalize();
        Path directory = Files.createDirectories(normalize);
        FileUtils.copyDirectory(mvnFolderPath.toFile(), directory.toFile());

        // Copy mvnw.cmd File to the User Project Directory
        Path mvnWindowsPath = extractedFilePath.resolve(MAVENW_WINDOWS);
        Files.copy(mvnWindowsExecutablePath, mvnWindowsPath.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);

        // Copy mvnw File to the User Project Directory
        Path mvnLinuxPath = extractedFilePath.resolve(MAVENW_LINUX);
        Files.copy(mvnLinuxExecutablePath, mvnLinuxPath.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
        log.info("");
    }

    private boolean isMavenWrapperNeeded(Path extractedFilePath) {
        return !findRootFile(extractedFilePath, ".mvn");
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