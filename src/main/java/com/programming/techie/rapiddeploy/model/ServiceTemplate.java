package com.programming.techie.rapiddeploy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "ServiceTemplate")
@Builder
public class ServiceTemplate {
    @Id
    private String id;
    private String guid;
    private String name;
    private String description;
    private Integer portNumber;
    private String imageName;
    private String tagName;
}
