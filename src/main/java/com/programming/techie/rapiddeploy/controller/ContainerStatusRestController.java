package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.BuildLogsResponse;
import com.programming.techie.rapiddeploy.service.application.ApplicationBuildService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/container/status")
@AllArgsConstructor
@Slf4j
public class ContainerStatusRestController {

    private final ApplicationBuildService applicationBuildService;

    @GetMapping("/{containerId}")
    public ResponseEntity<BuildLogsResponse> inspectBuild(@PathVariable String containerId) {
        return ResponseEntity.status(OK)
                .body(applicationBuildService.inspectBuild(containerId));
    }
}
