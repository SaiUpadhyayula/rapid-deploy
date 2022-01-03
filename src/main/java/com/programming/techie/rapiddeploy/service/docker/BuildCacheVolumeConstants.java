package com.programming.techie.rapiddeploy.service.docker;

import java.util.Map;

public class BuildCacheVolumeConstants {
    public static final Map<String, String> BUILD_CACHE_VOLUMES_MAP = Map.ofEntries(
            Map.entry("java", "/tmp/cache/.m2"));
}
