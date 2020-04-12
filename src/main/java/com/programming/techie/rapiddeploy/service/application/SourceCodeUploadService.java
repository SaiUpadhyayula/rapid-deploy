package com.programming.techie.rapiddeploy.service.application;

import com.programming.techie.rapiddeploy.dto.FileUploaded;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.files.FileUploadService;
import com.programming.techie.rapiddeploy.service.files.FileExtractorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class SourceCodeUploadService {
    private final ApplicationRepository applicationRepository;
    private final FileUploadService fileUploadService;
    private final FileExtractorService fileExtractorService;

    public void upload(MultipartFile file, String guid) {
        Application application = applicationRepository.findByGuid(guid)
                .orElseThrow(() -> new RapidDeployException("Cannot Find app by guid - " + guid));

        String fullFileName = fileUploadService.upload(file);
        String fileName = Objects.requireNonNull(file.getOriginalFilename()).replace(".zip", "");
        fileExtractorService.extractZipFile(new FileUploaded(application.getGuid(), fullFileName, fileName));
    }


}
