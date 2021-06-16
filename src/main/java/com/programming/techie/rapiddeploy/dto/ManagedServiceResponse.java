package com.programming.techie.rapiddeploy.dto;

import com.programming.techie.rapiddeploy.model.EnvironmentVariables;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagedServiceResponse {
    private String serviceTemplateGuid;
    private String managedServiceGuid;
    private String name;
    private List<EnvironmentVariables> environmentVariables;
}
