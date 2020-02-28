package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.events.FileExtracted;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FileExtractedListener {

    @EventListener
    public void handle(FileExtracted fileExtracted) {

    }
}
