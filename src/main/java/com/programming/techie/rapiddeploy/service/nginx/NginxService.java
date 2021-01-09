package com.programming.techie.rapiddeploy.service.nginx;

import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.service.docker.DockerContainerService;
import com.programming.techie.rapiddeploy.service.docker.DockerImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.NGINX;
import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.RAPID_DEPLOY_SERVICE_PREFIX;
import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
@Slf4j
public class NginxService {

    private static final String NGINX_CONTAINER_NAME = RAPID_DEPLOY_SERVICE_PREFIX + "nginx";

    private final DockerContainerService dockerContainerService;
    private final DockerImageService dockerImageService;
//    private final CertbotManager certbotManager;

    @SneakyThrows
    public void start(Boolean enableHttps) {
        if (nginxRunning()) {
            return;
        }
//        certbotManager.start();
//        certbotManager.ensureServiceIsRunning();

        dockerImageService.pullImage(NGINX, "latest");
        String nginxImageId = dockerImageService.getImageId(NGINX);
        log.info("Starting NGINX");
        DockerContainerPayload payload = DockerContainerPayload.builder()
                .name("nginx")
                .port(80)
                .exposedPort(80)
                .imageId(nginxImageId)
                .mountSource(resolvePathForDefaultNginxConf())
                .mountTarget("/etc/nginx/nginx.conf")
                .environmentVariables(emptyList())
                .volumes(Arrays.asList("web-root:/var/www/html",
                        "certbot-etc:/etc/letsencrypt",
                        "certbot-var:/var/lib/letsencrypt"))
                .build();
        dockerContainerService.run(payload);
    }

    private boolean nginxRunning() {
        return dockerContainerService.checkIfContainerIsRunning(NGINX_CONTAINER_NAME);
    }

    private String resolvePathForDefaultNginxConf() {
        try {
            URI uri = Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("example/nginx.conf"))
                    .toURI();
            return Paths.get(uri).toAbsolutePath().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Exception occurred while reading the default NGINX Conf file", e);
        }
    }
}
