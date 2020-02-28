package com.programming.techie.rapiddeploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class RapidDeployApplication {

    public static void main(String[] args) {
        SpringApplication.run(RapidDeployApplication.class, args);
    }

}
