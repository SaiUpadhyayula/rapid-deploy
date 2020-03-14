package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import com.programming.techie.rapiddeploy.service.ManagedServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/managed-service/")
@AllArgsConstructor
public class ManagedServiceRestController {

    private final ManagedServiceFacade managedServiceFacade;

    @PostMapping("init")
    public ResponseEntity<ServiceTemplateDto> init(@Valid @RequestBody ServiceTemplateDto serviceTemplatePayload) {
        return ResponseEntity.status(OK)
                .body(managedServiceFacade.initManagedService(serviceTemplatePayload));
    }

    @GetMapping("start/{managedServiceGuid")
    public ResponseEntity<String> startContainer(@PathVariable String managedServiceGuid) {
        managedServiceFacade.startManagedService(managedServiceGuid);
        return ResponseEntity.status(OK).body("Managed Service Started Successfully");
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody ManagedServicePayload managedServicePayload) {
        return ResponseEntity.status(OK).body(managedServiceFacade.createManagedService(managedServicePayload));
    }

    @PutMapping
    public void update(@Valid @RequestBody ManagedServicePayload managedServicePayload) {
        managedServiceFacade.updateManagedService(managedServicePayload);
    }

    @GetMapping("inspect/{managedServiceGuid}")
    public ResponseEntity<String> getLogs(@PathVariable String managedServiceGuid) {
        return ResponseEntity.status(OK).body(managedServiceFacade.inspect(managedServiceGuid));
    }
}
