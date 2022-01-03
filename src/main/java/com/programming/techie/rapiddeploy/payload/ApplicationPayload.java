package com.programming.techie.rapiddeploy.payload;

import com.programming.techie.rapiddeploy.model.EnvironmentVariables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationPayload {
    @NotBlank
    private String applicationName;
    private String guid;
    private Integer port;
    private List<EnvironmentVariables> environmentVariablesList;
}
