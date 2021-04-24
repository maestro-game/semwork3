package ru.itis.semwork3.repository;

import java.io.InputStream;

public interface ImageRepository {
    String get(Long id);

    void save(InputStream inputStream, Long id);
}
