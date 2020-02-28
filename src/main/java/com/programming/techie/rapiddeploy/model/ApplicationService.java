package com.programming.techie.rapiddeploy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "ApplicationService")
public class ApplicationService {
    @Id
    private String id;
    private String name;
    private String description;
}
