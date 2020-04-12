package com.programming.techie.rapiddeploy.service.docker;

import com.programming.techie.rapiddeploy.dto.YamlParsingCompleted;
import com.programming.techie.rapiddeploy.model.ManifestDefinition;
import com.programming.techie.rapiddeploy.model.SupportedLanguage;
import com.programming.techie.rapiddeploy.service.docker.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DockerfileCreationService {
    private final Map<SupportedLanguage, DockerfileFactory> dockerfileFactoryMap;
    private final DockerContainerOrchestrator dockerContainerOrchestrator;

    @PostConstruct
    public void initializeDockerfileFactoryMap() {
        dockerfileFactoryMap.put(SupportedLanguage.JAVA, new JavaDockerFileFactory());
        dockerfileFactoryMap.put(SupportedLanguage.NODEJS, new NodejsDockerFileFactory());
        dockerfileFactoryMap.put(SupportedLanguage.PYTHON, new PythonDockerfileFactory());
        dockerfileFactoryMap.put(SupportedLanguage.PHP, new PhpDockerfileFactory());
        dockerfileFactoryMap.put(SupportedLanguage.RUBY, new RubyDockerfileFactory());
    }

    public void create(YamlParsingCompleted yamlParsingCompleted) {
        Path extractedFilePath = yamlParsingCompleted.getExtractedFilePath();

        SupportedLanguage supportedLanguage = SupportedLanguage.lookup(yamlParsingCompleted.getManifestDefinition().getLanguage());
        DockerfileFactory dockerfileFactory = dockerfileFactoryMap.get(supportedLanguage);
        String dockerFileContent = dockerfileFactory.createDockerFileContent(extractedFilePath, yamlParsingCompleted.getManifestDefinition());
        File dockerFile = createDockerFile(extractedFilePath, dockerFileContent);
        createProcFile(extractedFilePath, yamlParsingCompleted.getManifestDefinition());
        dockerContainerOrchestrator.handle(dockerFile, yamlParsingCompleted.getAppGuid());
    }

    @SneakyThrows
    private File createProcFile(Path extractedFilePath, ManifestDefinition manifestDefinition) {
        Path dockerFilePath = Paths.get(extractedFilePath.toAbsolutePath().toString() + File.separator + "Procfile").toAbsolutePath();
        return Files.write(dockerFilePath, buildProcFileContent(manifestDefinition.getRun()).getBytes()).toFile();
    }

    private String buildProcFileContent(String content) {
        return "web: " + content;
    }

    @SneakyThrows
    private File createDockerFile(Path extractedFilePath, String dockerFileContent) {
        Path dockerFilePath = Paths.get(extractedFilePath.toAbsolutePath().toString() + File.separator + "Dockerfile").toAbsolutePath();
        return Files.write(dockerFilePath, dockerFileContent.getBytes()).toFile();
    }
}
