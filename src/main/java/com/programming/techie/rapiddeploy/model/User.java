package com.programming.techie.rapiddeploy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("rd_user")
public class User {
    @Id
    private String id;
    private String userName;
    private String password;
    private boolean enabled;
}
