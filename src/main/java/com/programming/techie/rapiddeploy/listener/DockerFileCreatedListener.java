package com.programming.techie.rapiddeploy.listener;

import com.programming.techie.rapiddeploy.events.DockerfileCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class DockerFileCreatedListener {

    @EventListener
    public void handle(DockerfileCreated dockerfileCreated) {

    }
}
