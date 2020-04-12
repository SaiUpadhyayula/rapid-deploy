package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.GitRequestPayload;
import com.programming.techie.rapiddeploy.service.git.GitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/source-code/git")
@AllArgsConstructor
@Slf4j
public class GitRestController {

    private final GitService gitService;

    @PostMapping("/clone")
    public void clone(@Valid @RequestBody GitRequestPayload gitRequestPayload) {
        gitService.cloneRepository(gitRequestPayload);
    }
}
