package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import com.programming.techie.rapiddeploy.service.ServiceManagerFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/service-template/")
@AllArgsConstructor
public class ServiceTemplateRestController {
    private final ServiceManagerFacade serviceManagerFacade;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ServiceTemplateDto serviceTemplateDto) {
        String guid = serviceManagerFacade.createServiceTemplate(serviceTemplateDto);
        return ResponseEntity.status(HttpStatus.OK).body("Service Template created successfully!! GUID - " + guid);
    }

    @GetMapping
    public ResponseEntity<List<ServiceTemplateDto>> getAll() {
        List<ServiceTemplateDto> serviceTemplates = serviceManagerFacade.getAllServiceTemplates();
        return ResponseEntity.status(HttpStatus.OK).body(serviceTemplates);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody ServiceTemplateDto serviceTemplateDto) {
        serviceManagerFacade.updateServiceTemplate(serviceTemplateDto);
        return ResponseEntity.status(HttpStatus.OK).body("Service Template updated successfully!!");
    }

    @DeleteMapping("/{guid}")
    public ResponseEntity<String> delete(@PathVariable String guid) {
        serviceManagerFacade.deleteServiceTemplate(guid);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Template Deleted Successfully!!");
    }
}
