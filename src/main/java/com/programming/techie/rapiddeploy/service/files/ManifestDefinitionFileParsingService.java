package com.programming.techie.rapiddeploy.service.files;

import com.programming.techie.rapiddeploy.dto.YamlParsingCompleted;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.ManifestDefinition;
import com.programming.techie.rapiddeploy.service.docker.DockerfileCreationService;
import com.programming.techie.rapiddeploy.util.RapidDeployConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.find;

@Service
@RequiredArgsConstructor
public class ManifestDefinitionFileParsingService {

    private final DockerfileCreationService dockerfileCreationService;

    public void parse(String guid, Path extractedFilePath) {
        Yaml yaml = new Yaml(new Constructor(ManifestDefinition.class));

        try {
            Path manifestFilePath = findManifestFile(extractedFilePath);
            InputStream inputStream = Files.newInputStream(manifestFilePath);
            ManifestDefinition manifestDefinition = yaml.load(inputStream);
            YamlParsingCompleted yamlParsingCompleted = new YamlParsingCompleted(manifestDefinition,
                    extractedFilePath, guid);
            dockerfileCreationService.create(yamlParsingCompleted);
        } catch (Exception e) {
            throw new RapidDeployException("Exception occurred while parsing Manifest Definition File", e);
        }
    }

    private Path findManifestFile(Path extractedFilePath) throws IOException {
        return find(extractedFilePath, 1,
                ((path, basicFileAttributes) -> path.getFileName().toString()
                        .equals(RapidDeployConstants.MANIFEST_FILE)))
                .findAny().orElseThrow(() -> new RapidDeployException("Cannot find manifest.yml file inside the folder, " +
                        "Please Check"));
    }
}
