package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import com.programming.techie.rapiddeploy.service.ServiceManagerFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/managed-service/")
@AllArgsConstructor
public class ManagedServiceRestController {

    private final ServiceManagerFacade serviceManagerFacade;

    @PostMapping("init")
    public ResponseEntity<ServiceTemplateDto> init(@Valid @RequestBody ServiceTemplateDto serviceTemplatePayload) {
        return ResponseEntity.status(OK)
                .body(serviceManagerFacade.initManagedService(serviceTemplatePayload));
    }

    @PostMapping
    public void create(@Valid @RequestBody ManagedServicePayload managedServicePayload) {
        serviceManagerFacade.createManagedService(managedServicePayload);
    }

    @PutMapping
    public void update(@Valid @RequestBody ManagedServicePayload managedServicePayload) {
        serviceManagerFacade.updateManagedService(managedServicePayload);
    }
}
