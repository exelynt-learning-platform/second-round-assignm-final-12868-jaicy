package com.multigenesis.ecomm_assesment.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        String originalFileName = file.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(
                originalFileName.substring(originalFileName.lastIndexOf('.'))
        );

        Files.createDirectories(Paths.get(path));

        String filePath = Paths.get(path, fileName).toString();

        Files.copy(
                file.getInputStream(),
                Paths.get(filePath),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );

        return fileName;
    }
}
