package com.programming.techie.rapiddeploy.listener;

import com.programming.techie.rapiddeploy.events.FileExtracted;
import com.programming.techie.rapiddeploy.events.YamlParsingCompleted;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.ManifestDefinition;
import com.programming.techie.rapiddeploy.model.SupportedLanguage;
import com.programming.techie.rapiddeploy.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.find;

@Service
@AllArgsConstructor
@Slf4j
public class FileExtractedListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void handle(FileExtracted fileExtracted) throws IOException {
        Yaml yaml = new Yaml(new Constructor(ManifestDefinition.class));

        Path extractedFilePath = fileExtracted.getExtractedFilePath();
        Path manifestFilePath = findManifestFile(extractedFilePath);

        InputStream inputStream = Files.newInputStream(manifestFilePath);
        ManifestDefinition manifestDefinition = yaml.load(inputStream);
        validateManifestDefinition(manifestDefinition);
        applicationEventPublisher.publishEvent(new YamlParsingCompleted(manifestDefinition, extractedFilePath));
    }

    private Path findManifestFile(Path extractedFilePath) throws IOException {
        return find(extractedFilePath, 1,
                ((path, basicFileAttributes) -> path.getFileName().toString()
                        .equals(FileUtils.MANIFEST_FILE)))
                .findAny().orElseThrow(() -> new RapidDeployException("Cannot find manifest.yml file inside the folder, Please Check"));
    }

    private void validateManifestDefinition(ManifestDefinition manifestDefinition) {
        if (!manifestDefinition.getLanguage().equals(SupportedLanguage.JAVA.getValue())) {
            throw new RapidDeployException(manifestDefinition.getLanguage() + " is not supported!! We only support Java for now");
        }
    }
}
