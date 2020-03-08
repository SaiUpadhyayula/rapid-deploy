package com.programming.techie.rapiddeploy.events;

import com.programming.techie.rapiddeploy.model.ManifestDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;

@Data
@AllArgsConstructor
public class YamlParsingCompleted {
    private ManifestDefinition manifestDefinition;
    private Path extractedFilePath;
    private String appGuid;
}
