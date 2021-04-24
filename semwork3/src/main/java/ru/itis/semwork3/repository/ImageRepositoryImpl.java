package ru.itis.semwork3.repository;

import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ImageRepositoryImpl implements ImageRepository {

    @Override
    public String get(Long id) {
        //TODO
        return null;
    }

    @Override
    public void save(InputStream inputStream, Long id) {
        //TODO
    }
}
