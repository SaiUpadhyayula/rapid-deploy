package com.programming.techie.rapiddeploy.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.util.StringUtils.cleanPath;

@UtilityClass
public class FileNameUtils {

    public static final String UPLOAD_DIR = "user-files";
    public static final String GIT_UPLOAD_DIR = "git-user-files";
    public static final String UNZIPPED_DIR = "unzipped-files";
    public static final String MANIFEST_FILE = "manifest.yml";
    public static final String POM_XML = "pom.xml";
    public static final String BUILD_GRADLE = "build.gradle";
    public static final String MAVEN_FOLDER = ".mvn";
    public static final String MAVENW_WINDOWS = "mvnw.cmd";
    public static final String MAVENW_LINUX = "mvnw";

    public static String extractFileName(MultipartFile file) {
        checkNotNull(file);
        checkNotNull(file.getOriginalFilename());
        return cleanPath(file.getOriginalFilename());
    }
}
