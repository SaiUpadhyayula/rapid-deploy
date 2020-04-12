package com.programming.techie.rapiddeploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAsync
@EnableSwagger2
@EnableMongoRepositories(basePackages = "com.programming.techie.rapiddeploy.repository")
public class RapidDeployApplication {

    public static void main(String[] args) {
        SpringApplication.run(RapidDeployApplication.class, args);
    }

}
