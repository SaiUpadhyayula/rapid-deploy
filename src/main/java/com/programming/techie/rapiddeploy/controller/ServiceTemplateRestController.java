package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import com.programming.techie.rapiddeploy.service.servicetemplate.ServiceTemplateFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin/service-template/")
@AllArgsConstructor
public class ServiceTemplateRestController {
    private final ServiceTemplateFacade serviceTemplateFacade;

    @PostMapping
    public String create(@RequestBody ServiceTemplateDto serviceTemplateDto) {
        String guid = serviceTemplateFacade.createServiceTemplate(serviceTemplateDto);
        return "Service Template created successfully!! GUID - " + guid;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ServiceTemplateDto> getAll() {
        return serviceTemplateFacade.getAllServiceTemplates();
    }

    @PutMapping
    @ResponseStatus(OK)
    public String update(@RequestBody ServiceTemplateDto serviceTemplateDto) {
        serviceTemplateFacade.updateServiceTemplate(serviceTemplateDto);
        return "Service Template updated successfully!!";
    }

    @DeleteMapping("/{guid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete(@PathVariable String guid) {
        serviceTemplateFacade.deleteServiceTemplate(guid);
        return "Service Template Deleted Successfully!!";
    }
}
