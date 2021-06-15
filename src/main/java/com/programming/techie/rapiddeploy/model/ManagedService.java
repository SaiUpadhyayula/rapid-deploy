package com.programming.techie.rapiddeploy.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(value = "ManagedService")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagedService extends IdEntity {
    private String name;
    private String guid;
    private ServiceTemplate serviceTemplate;
    private List<EnvironmentVariables> environmentVariables;
    private String logs;
    private String containerId;
}
