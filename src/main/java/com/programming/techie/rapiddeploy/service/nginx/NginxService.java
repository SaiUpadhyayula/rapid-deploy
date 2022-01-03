package com.programming.techie.rapiddeploy.service.nginx;

import com.github.dockerjava.api.model.MountType;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.service.certbot.CertbotManager;
import com.programming.techie.rapiddeploy.service.docker.DockerContainerService;
import com.programming.techie.rapiddeploy.service.docker.DockerImageService;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.NGINX;
import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.RAPID_DEPLOY_SERVICE_PREFIX;
import static java.nio.charset.Charset.forName;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
@Slf4j
public class NginxService {

    private static final String NGINX_CONTAINER_NAME = RAPID_DEPLOY_SERVICE_PREFIX + "nginx";

    private final DockerContainerService dockerContainerService;
    private final DockerImageService dockerImageService;
    private final CertbotManager certbotManager;

    @SneakyThrows
    public void start() {
        if (nginxRunning()) {
            return;
        }
//        certbotManager.start();
//        certbotManager.ensureServiceIsRunning();

        dockerImageService.pullImage(NGINX, "latest");
        String nginxImageId = dockerImageService.getImageId(NGINX);
        log.info("Starting NGINX");
        DockerContainerPayload payload = DockerContainerPayload.builder()
                .name(RAPID_DEPLOY_SERVICE_PREFIX + "nginx")
                .port(80)
                .exposedPort(80)
                .imageId(nginxImageId)
                .volumeList(singletonList(new DockerContainerPayload.Volume(resolveNginxConfigFile(), "/etc/nginx/nginx.conf", MountType.BIND)))
                .environmentVariables(emptyList())
                .build();
        dockerContainerService.run(payload);
    }

    private boolean nginxRunning() {
        return dockerContainerService.checkIfContainerIsRunning(NGINX_CONTAINER_NAME);
    }

    private String resolveNginxConfigFile() {
        try {
            return Paths.get("nginx/nginx.conf").toAbsolutePath().toString();
        } catch (Exception e) {
            throw new RapidDeployException("Exception occurred while reading the default NGINX Conf file", e);
        }
    }

    public void createConfigFile(String name, Integer port) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource("example/nginx.conf-template.mustache")).getFile());
            String templateData = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            Template template = Mustache.compiler().compile(templateData);
            Map<String, String> templateMap = new HashMap<>();
            templateMap.put("applicationName", name);
            templateMap.put("port", port.toString());

            String data = template.execute(templateMap);

            FileUtils.writeStringToFile(new File("nginx/nginx.conf"), data, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RapidDeployException("Exception occurred while creating Nginx Conf file");
        }

    }
}
