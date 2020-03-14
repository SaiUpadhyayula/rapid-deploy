package com.programming.techie.rapiddeploy.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class IdEntity {
    @Id
    private String id;
}
