package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.service.FileUploadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
public class FileUploadController {

    private FileUploadService fileUploadService;

    @PostMapping("/api/{appName}/files")
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, @PathVariable String appName) {
        log.info("Uploading file");
        fileUploadService.upload(file, appName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
