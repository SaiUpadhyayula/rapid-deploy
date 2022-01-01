package com.programming.techie.rapiddeploy.service.application;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.google.common.base.Preconditions;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.model.BuildState;
import com.programming.techie.rapiddeploy.payload.BuildLogsResponse;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.docker.DockerClientManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationBuildService {

    private final ApplicationRepository applicationRepository;

    public BuildLogsResponse inspectBuild(String containerId) {
        Application application = applicationRepository.findByContainerId(containerId)
                .orElseThrow(() -> new RapidDeployException("Cannot find application" +
                        " with container id - " + containerId));

        DockerClient client = DockerClientManager.getClient();
        InspectContainerResponse exec = client.inspectContainerCmd(containerId).exec();
        BuildState buildState = determineBuildStatus(application, exec);
        String buildLogs = collectBuildLogs(containerId, client);

        return new BuildLogsResponse(buildState, containerId, application.getGuid(), buildLogs);
    }

    String collectBuildLogs(String containerId, DockerClient client) {
        StringBuilder stringBuilder = new StringBuilder();
        LogContainerResultCallback callback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                stringBuilder.append(item.toString())
                        .append(System.lineSeparator());
            }
        };
        try {
            client.logContainerCmd(containerId)
                    .withStdErr(true)
                    .withStdOut(true)
                    .withFollowStream(true)
                    .withTail(5)
                    .exec(callback)
                    .awaitCompletion(5, SECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RapidDeployException("Exception Occurred when collecting Build Logs");
        }
        return stringBuilder.toString();
    }

    //TODO: Rename BuildState to Container State
    private BuildState determineBuildStatus(Application application, InspectContainerResponse exec) {
        Preconditions.checkNotNull(exec.getState());
        if (Objects.equals(exec.getState().getStatus(), "exited")) {
            if (exec.getState().getExitCode() == 0) {
                application.setBuildState(BuildState.SUCCESS);
            } else {
                application.setBuildState(BuildState.FAILED);
            }
        } else {
            application.setBuildState(BuildState.IN_PROGRESS);
        }
        return application.getBuildState();
    }
}
