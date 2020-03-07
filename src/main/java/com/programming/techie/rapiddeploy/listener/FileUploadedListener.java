package com.programming.techie.rapiddeploy.listener;

import com.programming.techie.rapiddeploy.events.FileExtracted;
import com.programming.techie.rapiddeploy.events.FileUploaded;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

import static com.programming.techie.rapiddeploy.util.FileNameUtils.UNZIPPED_DIR;

@Service
@AllArgsConstructor
@Slf4j
public class FileUploadedListener {
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void handle(FileUploaded fileUploaded) {
        String fullFileName = extractZipFile(fileUploaded);
        applicationEventPublisher.publishEvent(new FileExtracted(fileUploaded.getAppName(), Paths.get(fullFileName)));
    }

    private String extractZipFile(FileUploaded fileUploaded) {
        String fullFileName = fileUploaded.getFullFileName();
        try {
            ZipFile zipFile = new ZipFile(fullFileName);
            String destination = Paths.get(UNZIPPED_DIR).toAbsolutePath().toString();
            zipFile.extractAll(destination);
            return destination + "\\" + fileUploaded.getFileName();
        } catch (ZipException e) {
            throw new RapidDeployException("Exception occurred while extracting zip file", e);
        }
    }
}

