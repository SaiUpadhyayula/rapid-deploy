package com.programming.techie.rapiddeploy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private String applicationName;
    private String guid;
}
