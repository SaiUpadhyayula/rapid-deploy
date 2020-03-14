package com.programming.techie.rapiddeploy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagedServicePayload {
    private String serviceTemplateGuid;
    private String managedServiceGuid;
    private String name;
    private List<EnvironmentVariables> environmentVariables;
}
