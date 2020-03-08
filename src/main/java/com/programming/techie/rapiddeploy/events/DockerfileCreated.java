package com.programming.techie.rapiddeploy.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DockerfileCreated {
    private File file;
    private String appGuid;
}
