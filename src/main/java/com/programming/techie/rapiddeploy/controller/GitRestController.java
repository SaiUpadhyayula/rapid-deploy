package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.GitRequestPayload;
import com.programming.techie.rapiddeploy.service.git.GitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/source-code/git")
@AllArgsConstructor
@Slf4j
public class GitRestController {

    private final GitService gitService;

    @PostMapping("/clone")
    @ResponseStatus(OK)
    public String clone(@Valid @RequestBody GitRequestPayload gitRequestPayload) {
        gitService.cloneRepository(gitRequestPayload);
        return "Repository cloned successfully, please wait while the application is being started";
    }
}
