package com.programming.techie.rapiddeploy.listener;

import com.programming.techie.rapiddeploy.events.DockerfileCreated;
import com.programming.techie.rapiddeploy.events.YamlParsingCompleted;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.ManifestDefinition;
import com.programming.techie.rapiddeploy.model.SupportedLanguage;
import com.programming.techie.rapiddeploy.service.DockerfileFactory;
import com.programming.techie.rapiddeploy.service.impl.dockerfile.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.readAllBytes;

@Service
@AllArgsConstructor
@Slf4j
public class YamlParsingCompletedListener {

    private final Map<SupportedLanguage, DockerfileFactory> dockerfileFactoryMap;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    public void initializeDockerfileFactoryMap() {
        dockerfileFactoryMap.put(SupportedLanguage.JAVA, new JavaDockerFileFactory());
        dockerfileFactoryMap.put(SupportedLanguage.NODEJS, new NodejsDockerFileFactory());
        dockerfileFactoryMap.put(SupportedLanguage.PYTHON, new PythonDockerfileFactory());
        dockerfileFactoryMap.put(SupportedLanguage.PHP, new PhpDockerfileFactory());
        dockerfileFactoryMap.put(SupportedLanguage.RUBY, new RubyDockerfileFactory());
    }

    @EventListener
    public void handle(YamlParsingCompleted yamlParsingCompleted) {
        Path extractedFilePath = yamlParsingCompleted.getExtractedFilePath();

        SupportedLanguage supportedLanguage = SupportedLanguage.lookup(yamlParsingCompleted.getManifestDefinition().getLanguage());
        DockerfileFactory dockerfileFactory = dockerfileFactoryMap.get(supportedLanguage);
        String dockerFileContent = dockerfileFactory.createDockerFileContent(extractedFilePath, yamlParsingCompleted.getManifestDefinition());
        File dockerFile = createDockerFile(extractedFilePath, dockerFileContent);
        createProcFile(extractedFilePath, yamlParsingCompleted.getManifestDefinition());
        applicationEventPublisher.publishEvent(new DockerfileCreated(dockerFile, yamlParsingCompleted.getAppGuid()));
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
