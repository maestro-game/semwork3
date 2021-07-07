package ru.itis.semwork3.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class ImageRepositoryImpl implements ImageRepository {
    @Value("${server.image-folder}")
    String imageFolder;
    String prefix = "/avatars/";

    @Override
    public String get(String id) {
        var files = new File(imageFolder).list(
                (dir, name) -> name.startsWith(id + ".")
        );
        if (files != null && files.length > 0) {
            return prefix + files[0];
        } else {
            return null;
        }
    }

    @Override
    public String save(MultipartFile multipartFile, String id) throws IOException {
        String fileName = id + "." + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf('.') + 1);
        multipartFile.transferTo(Path.of(imageFolder + fileName));
        return prefix + fileName;
    }

    @Override
    public void delete(String id) {
        String fileName = get(id);
        new File(imageFolder + fileName.substring(fileName.lastIndexOf('/'))).delete();
    }
}
