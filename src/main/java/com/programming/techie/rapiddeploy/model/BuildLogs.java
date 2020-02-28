package com.programming.techie.rapiddeploy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildLogs {
    private String id;
    private String text;
}
