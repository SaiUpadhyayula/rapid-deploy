package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.BuildLogsResponse;
import com.programming.techie.rapiddeploy.service.application.ApplicationBuildService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/container/status")
@AllArgsConstructor
@Slf4j
public class ContainerStatusRestController {

    private final ApplicationBuildService applicationBuildService;

    @GetMapping("/{containerId}")
    @ResponseStatus(OK)
    public BuildLogsResponse inspectBuild(@PathVariable String containerId) {
        return applicationBuildService.inspectBuild(containerId);
    }
}
