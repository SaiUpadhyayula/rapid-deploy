package com.programming.techie.rapiddeploy.mapper;

import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import org.springframework.stereotype.Service;

@Service
public class ApplicationMapper {

    public ApplicationResponse map(Application application) {
        return ApplicationResponse.builder()
                .applicationName(application.getName())
                .guid(application.getGuid())
                .build();
    }
}
