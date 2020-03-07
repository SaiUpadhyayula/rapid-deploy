package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.GitRequestPayload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/rapid-deploy/git/source-code")
@AllArgsConstructor
@Slf4j
public class GitRestController {

    @PostMapping
    public void download(@Valid @RequestBody GitRequestPayload gitRequestPayload) {

    }
}
