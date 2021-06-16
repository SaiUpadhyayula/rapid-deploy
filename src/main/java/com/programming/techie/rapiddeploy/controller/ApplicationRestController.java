package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.ApplicationPayload;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import com.programming.techie.rapiddeploy.payload.FileUploadResponse;
import com.programming.techie.rapiddeploy.service.application.ApplicationService;
import com.programming.techie.rapiddeploy.service.application.SourceCodeUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
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
    @ResponseStatus(CREATED)
    public ApplicationResponse create(@Valid @RequestBody ApplicationPayload applicationPayload) {
        return applicationService.create(applicationPayload);
    }

    @PutMapping
    @ResponseStatus(OK)
    public ApplicationResponse update(@Valid @RequestBody ApplicationPayload applicationPayload) {
        return applicationService.update(applicationPayload);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ApplicationResponse> getAll() {
        return applicationService.getAll();
    }

    @GetMapping("/{guid}")
    @ResponseStatus(OK)
    public ApplicationResponse getOne(@PathVariable String guid) {
        return applicationService.getOne(guid);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void delete(@RequestParam("guid") String guid) {
        applicationService.delete(guid);
    }

    @GetMapping("/{guid}/start")
    @ResponseStatus(OK)
    public String startApplication(@PathVariable String guid) {
        return applicationService.startApplicationContainer(guid);
    }

    @GetMapping("/{guid}/stop")
    @ResponseStatus(OK)
    public String stopApplication(@PathVariable String guid) {
        applicationService.stopApplicationContainer(guid);
        return "Application Stopped";
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
    @ResponseStatus(OK)
    public String getContainerId(@PathVariable String guid) {
        return applicationService.getContainerId(guid);
    }

    @GetMapping("/{appName}/container")
    @ResponseStatus(OK)
    public String getContainerIdByAppName(@PathVariable String appName) {
        return applicationService.getContainerIdByAppName(appName);
    }
}
