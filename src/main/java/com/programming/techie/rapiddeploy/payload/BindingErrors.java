package com.programming.techie.rapiddeploy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindingErrors {
    private String fieldName;
    private String fieldError;
}
