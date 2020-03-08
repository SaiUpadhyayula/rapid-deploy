package com.programming.techie.rapiddeploy.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploaded {
    private String guid;
    private String fullFileName;
    private String fileName;
}
