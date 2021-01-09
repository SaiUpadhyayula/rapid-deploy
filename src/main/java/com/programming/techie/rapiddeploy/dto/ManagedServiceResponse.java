package com.programming.techie.rapiddeploy.dto;

import com.programming.techie.rapiddeploy.model.EnvironmentVariables;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagedServiceResponse {
    private String serviceTemplateGuid;
    private String managedServiceGuid;
    private String name;
    private List<EnvironmentVariables> environmentVariables;
}
