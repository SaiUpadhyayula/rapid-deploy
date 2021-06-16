package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.dto.ManagedServiceResponse;
import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import com.programming.techie.rapiddeploy.service.managedservice.ManagedServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/managed-service/")
@AllArgsConstructor
public class ManagedServiceRestController {

    private final ManagedServiceFacade managedServiceFacade;

    @PostMapping("init")
    @ResponseStatus(OK)
    public ServiceTemplateDto init(@Valid @RequestBody ServiceTemplateDto serviceTemplatePayload) {
        return managedServiceFacade.initManagedService(serviceTemplatePayload);
    }

    @GetMapping("start/{managedServiceGuid}")
    @ResponseStatus(OK)
    public String startContainer(@PathVariable String managedServiceGuid) {
        managedServiceFacade.startManagedService(managedServiceGuid);
        return "Managed Service Started Successfully";
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public String create(@Valid @RequestBody ManagedServicePayload managedServicePayload) {
        return managedServiceFacade.createManagedService(managedServicePayload);
    }

    @PutMapping
    @ResponseStatus(OK)
    public void update(@Valid @RequestBody ManagedServicePayload managedServicePayload) {
        managedServiceFacade.updateManagedService(managedServicePayload);
    }

    @GetMapping("inspect/{managedServiceGuid}")
    @ResponseStatus(OK)
    public String getLogs(@PathVariable String managedServiceGuid) {
        return managedServiceFacade.inspect(managedServiceGuid);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ManagedServiceResponse> getAllManagedServices() {
        return managedServiceFacade.getAll();
    }
}
