package com.programming.techie.rapiddeploy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTemplateDto {
    private String name;
    private String guid;
    private String description;
    private Integer portNumber;
    private String imageName;
    private String tagName;
}
