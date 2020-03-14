package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.exceptions.FileStorageException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.programming.techie.rapiddeploy.util.FileNameUtils.UPLOAD_DIR;
import static com.programming.techie.rapiddeploy.util.FileNameUtils.extractFileName;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@AllArgsConstructor
@Slf4j
public class FileStorageService {

    public String upload(MultipartFile file) {
        String fileName = extractFileName(file);
        checkForInvalidCharactersInFileName(fileName);
        Path targetLocation = createTargetDir(fileName);
        try {
            copy(file.getInputStream(), targetLocation, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Exception Occurred when copying file");
        }
        return targetLocation.toAbsolutePath().toString();
    }

    private Path createTargetDir(String fileName) {
        Path fileUploadDirectory = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileUploadDirectory);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored");
        }
        return fileUploadDirectory.resolve(fileName);
    }

    private void checkForInvalidCharactersInFileName(String fileName) {
        if (fileName.contains(".."))
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
    }
}
