package com.programming.techie.rapiddeploy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {
    private String containerId;
    private String message;
}
