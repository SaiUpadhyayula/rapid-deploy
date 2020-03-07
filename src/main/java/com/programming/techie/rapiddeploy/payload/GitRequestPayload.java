package com.programming.techie.rapiddeploy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitRequestPayload {
    @NotBlank(message = "GIT URL cannot be empty or null")
    private String gitURL;
}
