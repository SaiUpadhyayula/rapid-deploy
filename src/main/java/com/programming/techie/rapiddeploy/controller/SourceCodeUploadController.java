package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.BuildLogsResponse;
import com.programming.techie.rapiddeploy.payload.FileUploadResponse;
import com.programming.techie.rapiddeploy.service.ApplicationBuildService;
import com.programming.techie.rapiddeploy.service.SourceCodeUploadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/source-code")
@AllArgsConstructor
@Slf4j
public class SourceCodeUploadController {

    private final SourceCodeUploadService sourceCodeUploadService;
    private final ApplicationBuildService applicationBuildService;

    @PostMapping("/app/{appName}")
    public ResponseEntity<FileUploadResponse> upload(@RequestParam("file") MultipartFile file, @PathVariable String appName) {
        String containerId = sourceCodeUploadService.upload(file, appName);
        return ResponseEntity.status(CREATED)
                .body(new FileUploadResponse(containerId, "Source Code Uploaded. Please wait while the source code is building..."));
    }

    @GetMapping("/app/container/{containerId}")
    public ResponseEntity<BuildLogsResponse> inspectBuild(@PathVariable String containerId) {
        return ResponseEntity.status(OK)
                .body(applicationBuildService.inspectBuild(containerId));
    }
}
