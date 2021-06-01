package ru.itis.semwork3.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageRepository {
    String get(String id);

    String save(MultipartFile multipartFile, String id) throws IOException;
}
