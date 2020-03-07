package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.service.SourceCodeUploadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/source-code")
@AllArgsConstructor
@Slf4j
public class SourceCodeUploadController {

    private SourceCodeUploadService sourceCodeUploadService;

    @PostMapping("/{appName}")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @PathVariable String appName) {
        sourceCodeUploadService.upload(file, appName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Source Code Uploaded and Built Successfully!");
    }

    @ExceptionHandler(RapidDeployException.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
