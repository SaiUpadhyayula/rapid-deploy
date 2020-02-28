package com.programming.techie.rapiddeploy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeploymentHistory {
    private String id;
    private Integer versionNumber;
    private DeploymentState deploymentState;
    private Instant deploymentStartDate;
    private Instant deploymentEndDate;
    private String elapsedDuration;
}
