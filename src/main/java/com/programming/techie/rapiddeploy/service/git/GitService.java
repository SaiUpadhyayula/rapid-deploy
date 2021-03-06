package com.programming.techie.rapiddeploy.service.git;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.payload.GitRequestPayload;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.files.ManifestDefinitionFileParsingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.GIT_UPLOAD_DIR;

@Service
@AllArgsConstructor
@Slf4j
public class GitService {

    private final ApplicationRepository applicationRepository;
    private final ManifestDefinitionFileParsingService manifestDefinitionFileParsingService;

    public void cloneRepository(GitRequestPayload gitRequestPayload) {
        Application application = applicationRepository.findByGuid(gitRequestPayload.getAppGuid())
                .orElseThrow(() -> new RapidDeployException("Application not found with GUID - " + gitRequestPayload.getAppGuid()));
        File file = Paths.get(GIT_UPLOAD_DIR + File.separator + application.getName()).toFile();

        try {
            Git.cloneRepository()
                    .setURI(gitRequestPayload.getGitURL())
                    .setDirectory(file)
                    .call();
        } catch (Exception e) {
            log.error("Exception occurred when Cloning the repository", e);
            throw new RapidDeployException("Exception occurred when Cloning the repository", e);
        }

        manifestDefinitionFileParsingService.parse(application.getGuid(), file.toPath());
    }
}
