package com.programming.techie.rapiddeploy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "Application")
public class Application {
    @Id
    private String id;
    private String deployedUrl;
    private ApplicationState applicationState;
    private String gitUrl;
    private DeploymentHistory deploymentHistory;
    private BuildLogs buildLogs;
}