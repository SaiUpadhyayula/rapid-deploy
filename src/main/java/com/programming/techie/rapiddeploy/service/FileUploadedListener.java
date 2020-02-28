package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.events.FileExtracted;
import com.programming.techie.rapiddeploy.events.FileUploaded;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.programming.techie.rapiddeploy.util.FileUtils.UNZIPPED_DIR;

@Service
@AllArgsConstructor
@Slf4j
public class FileUploadedListener {
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void handle(FileUploaded fileUploaded) {
        File file = loadFile(fileUploaded.getFileName());
        try {
            ZipFile zipFile = new ZipFile(file);
            zipFile.stream()
                    .map(e -> unzipFile(zipFile, e))
                    .findFirst()
                    .orElseThrow(() -> new RapidDeployException(""));
        } catch (IOException e) {
            throw new RapidDeployException("");
        }
        applicationEventPublisher.publishEvent(new FileExtracted());
    }

    public File loadFile(String fileName) {
        Path filePath = Paths.get(UNZIPPED_DIR)
                .toAbsolutePath().normalize().resolve(fileName).normalize();
        return filePath.toFile();
    }

    public Path unzipFile(ZipFile zipFile, ZipEntry zipEntry) {
        try {
            Path targetPath = Paths.get(UNZIPPED_DIR)
                    .toAbsolutePath().normalize().resolve(Paths.get(zipEntry.getName()));
            if (Files.isDirectory(targetPath)) {
                Files.createDirectories(targetPath);
            } else {
                Files.createDirectories(targetPath.getParent());
                try (InputStream in = zipFile.getInputStream(zipEntry)) {
                    Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return targetPath;
        } catch (IOException e) {
            throw new RapidDeployException("Error processing zip entry '" + zipEntry.getName());
        }
    }
}

