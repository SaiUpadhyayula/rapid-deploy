package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.ApplicationPayload;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import com.programming.techie.rapiddeploy.payload.FileUploadResponse;
import com.programming.techie.rapiddeploy.service.application.ApplicationService;
import com.programming.techie.rapiddeploy.service.application.SourceCodeUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/application")
@AllArgsConstructor
public class ApplicationRestController {

    private final ApplicationService applicationService;
    private final SourceCodeUploadService sourceCodeUploadService;


    @PostMapping
    public ResponseEntity<ApplicationResponse> create(@Validated @RequestBody ApplicationPayload applicationPayload) {
        return ResponseEntity.status(CREATED)
                .body(applicationService.create(applicationPayload));
    }

    @PutMapping
    public ResponseEntity<ApplicationResponse> update(@Validated @RequestBody ApplicationPayload applicationPayload) {
        applicationService.update(applicationPayload);
        return null;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll() {
        return ResponseEntity.status(OK).body(applicationService.getAll());
    }

    @GetMapping("/{guid}")
    public ResponseEntity<ApplicationResponse> getOne(@PathVariable String guid) {
        return ResponseEntity.status(OK).body(applicationService.getOne(guid));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("guid") String guid) {
        applicationService.delete(guid);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/{guid}/start")
    public ResponseEntity<String> startApplication(@PathVariable String guid) {
        return ResponseEntity.status(OK).body(applicationService.startApplicationContainer(guid));
    }

    @GetMapping("/{guid}/stop")
    public ResponseEntity<String> stopApplication(@PathVariable String guid) {
        applicationService.stopApplicationContainer(guid);
        return ResponseEntity.status(OK).body("Application Stopped");
    }

    @PostMapping("/source-code/app/{guid}")
    public ResponseEntity<FileUploadResponse> upload(@RequestParam("file") MultipartFile file,
                                                     @PathVariable String guid, UriComponentsBuilder uriComponentsBuilder) {
        sourceCodeUploadService.upload(file, guid);
        Map<String, String> params = new HashMap<>();
        params.put("guid", guid);
        URI location = uriComponentsBuilder.path("/api/application/{guid}/container")
                .buildAndExpand(params)
                .toUri();
        return ResponseEntity.status(ACCEPTED)
                .location(location)
                .body(new FileUploadResponse(null, "Source Code Uploaded. " +
                        "Please wait while the source code is building..."));
    }

    @GetMapping("/{guid}/container")
    public ResponseEntity<String> getContainerId(@PathVariable String guid) {
        return ResponseEntity.status(OK)
                .body(applicationService.getContainerId(guid));
    }

    @GetMapping("/{appName}/container")
    public ResponseEntity<String> getContainerIdByAppName(@PathVariable String appName) {
        return ResponseEntity.status(OK)
                .body(applicationService.getContainerIdByAppName(appName));
    }
}
