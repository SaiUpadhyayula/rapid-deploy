package com.programming.techie.rapiddeploy.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceTemplateDto {
    private String name;
    private String guid;
    private String description;
    private Integer portNumber;
    private String imageName;
    private String tagName;
}
