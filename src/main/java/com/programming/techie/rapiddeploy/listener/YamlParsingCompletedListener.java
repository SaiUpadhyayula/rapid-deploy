package com.programming.techie.rapiddeploy.listener;

import com.programming.techie.rapiddeploy.events.DockerfileCreated;
import com.programming.techie.rapiddeploy.events.YamlParsingCompleted;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.SupportedLanguage;
import com.programming.techie.rapiddeploy.service.DockerfileFactory;
import com.programming.techie.rapiddeploy.service.impl.*;
import lombok.AllArgsConstructor;
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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.find;
import static java.nio.file.Files.readAllBytes;

@Service
@AllArgsConstructor
@Slf4j
public class YamlParsingCompletedListener {

    private Map<SupportedLanguage, DockerfileFactory> dockerfileFactoryMap;
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
        // Determine which Base Image we need to use in the Dockerfile
        String baseImage = determineBaseImage(yamlParsingCompleted);
        Path extractedFilePath = yamlParsingCompleted.getExtractedFilePath();

        SupportedLanguage supportedLanguage = SupportedLanguage.valueOf(yamlParsingCompleted.getManifestDefinition().getLanguage());
        DockerfileFactory dockerfileFactory = dockerfileFactoryMap.get(supportedLanguage);
        String dockerFileContent = dockerfileFactory.createDockerFileContent(extractedFilePath, baseImage);
        File dockerFile = dockerfileFactory.createDockerFile(dockerFileContent);

        applicationEventPublisher.publishEvent(new DockerfileCreated(dockerFile));
    }

    private String determineBaseImage(YamlParsingCompleted yamlParsingCompleted) {
        String language = yamlParsingCompleted.getManifestDefinition().getLanguage();
        String version = yamlParsingCompleted.getManifestDefinition().getVersion();

        try {
            File file = ResourceUtils.getFile("classpath:runtimes.json");
            JSONObject jsonObject = new JSONObject(new String(readAllBytes(file.toPath())));
            JSONArray jsonArray = (JSONArray) jsonObject.get(language);
            return jsonArray.toList()
                    .stream()
                    .map(object -> ((HashMap) object).get(version).toString())
                    .findAny()
                    .orElseThrow(() -> new RapidDeployException("Unsupported language/version - "
                            + language + ", " + version + ". Please Check your manifest file"));
        } catch (IOException e) {
            throw new RapidDeployException("");
        }
    }
}
