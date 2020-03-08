package com.programming.techie.rapiddeploy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private List<BindingErrors> bindingErrors;
}
