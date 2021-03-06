package com.programming.techie.rapiddeploy.util;

import org.springframework.web.multipart.MultipartFile;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.util.StringUtils.cleanPath;

public class RapidDeployConstants {

    //URLS
    public static final String CONTAINER_STATUS_BASE_URL = "/api/container/status";


    public static final String UPLOAD_DIR = "user-files";
    public static final String GIT_UPLOAD_DIR = "git-user-files";
    public static final String UNZIPPED_DIR = "unzipped-files";
    public static final String MANIFEST_FILE = "manifest.yml";
    public static final String POM_XML = "pom.xml";
    public static final String BUILD_GRADLE = "build.gradle";
    public static final String MAVEN_FOLDER = ".mvn";
    public static final String MAVEN_WRAPPER_WINDOWS = "mvnw.cmd";
    public static final String MAVEN_WRAPPER_LINUX = "mvnw";

    public static final String NETWORK_NAME = "rapid-deploy-network";
    public static final String RAPID_DEPLOY_SERVICE_PREFIX = "rd-srv-";
    public static final String NGINX = "nginx";

    public static String extractFileName(MultipartFile file) {
        checkNotNull(file);
        checkNotNull(file.getOriginalFilename());
        return cleanPath(file.getOriginalFilename());
    }
}
