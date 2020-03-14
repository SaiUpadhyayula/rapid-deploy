package com.programming.techie.rapiddeploy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(value = "ManagedService")
@AllArgsConstructor
@NoArgsConstructor
public class ManagedService extends IdEntity {
    private String name;
    private String guid;
    private ServiceTemplate serviceTemplate;
    private List<EnvironmentVariables> environmentVariables;
    private String logs;
    private String containerId;
}
