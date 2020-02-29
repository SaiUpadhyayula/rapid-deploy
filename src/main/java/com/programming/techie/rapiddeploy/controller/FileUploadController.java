package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.service.FileUploadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
@Slf4j
public class FileUploadController {

    private FileUploadService fileUploadService;

    @PostMapping("/{appName}")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @PathVariable String appName) {
        fileUploadService.upload(file, appName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("File Upload Successful");
    }
}
