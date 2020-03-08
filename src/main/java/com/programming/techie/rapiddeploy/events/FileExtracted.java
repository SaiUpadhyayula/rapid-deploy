package com.programming.techie.rapiddeploy.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileExtracted {
    private String guid;
    private Path extractedFilePath;
}
