package com.programming.techie.rapiddeploy.mapper;

import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(target = "applicationName", source = "name")
    ApplicationResponse map(Application application);
}
