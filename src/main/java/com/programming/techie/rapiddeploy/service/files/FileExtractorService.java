package com.programming.techie.rapiddeploy.service.files;

import com.programming.techie.rapiddeploy.dto.FileUploaded;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.UNZIPPED_DIR;

@Service
@RequiredArgsConstructor
public class FileExtractorService {

    private final ManifestDefinitionFileParsingService manifestDefinitionFileParsingService;

    @Async
    public void extractZipFile(FileUploaded fileUploaded) {
        String fullFileName = fileUploaded.getFullFileName();
        try {
            ZipFile zipFile = new ZipFile(fullFileName);
            String destination = Paths.get(UNZIPPED_DIR).toAbsolutePath().toString();
            zipFile.extractAll(destination);
            String updatedFullFileName = String.format("%s%s%s", destination, File.separator,fileUploaded.getFileName());
            manifestDefinitionFileParsingService.parse(fileUploaded.getGuid(), Paths.get(updatedFullFileName));
        } catch (ZipException e) {
            throw new RapidDeployException("Exception occurred while extracting zip file", e);
        }
    }
}
