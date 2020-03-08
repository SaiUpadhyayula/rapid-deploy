package com.programming.techie.rapiddeploy.payload;

import com.programming.techie.rapiddeploy.model.BuildState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildLogsResponse {
    private BuildState buildState;
    private String containerId;
    private String appGuid;
    private String buildLogs;
}
