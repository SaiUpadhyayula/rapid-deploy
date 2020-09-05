package com.programming.techie.rapiddeploy.service.certbot;

import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.service.docker.DockerContainerService;
import com.programming.techie.rapiddeploy.service.docker.DockerImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertbotManager {

    private final DockerImageService dockerImageService;
    private final DockerContainerService dockerContainerService;

    public void start() {
//        dockerImageService.pullImage("certbot", "latest");
//        String certbotImageId = dockerImageService.getImageId("certbot");
//        log.info("Starting Certbot Instance");
//
//        DockerContainerPayload payload = DockerContainerPayload.builder()
//                .name("nginx")
//                .port(80)
//                .exposedPort(80)
//                .imageId(certbotImageId)
//                .mountSource(resolvePathForDefaultNginxConf())
//                .mountTarget("/etc/nginx/nginx.conf")
//                .environmentVariables(emptyList())
//                .build();
    }

    public void ensureServiceIsRunning() {

    }
}
