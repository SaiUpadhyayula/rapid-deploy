package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.ApplicationPayload;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import com.programming.techie.rapiddeploy.service.ApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/application")
@AllArgsConstructor
public class ApplicationRestController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(@Valid @RequestBody ApplicationPayload applicationPayload) {
        return ResponseEntity.status(CREATED)
                .body(applicationService.create(applicationPayload));
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
        return ResponseEntity.status(NOT_FOUND).build();
    }
}
