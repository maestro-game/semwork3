package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.semwork3.repository.ImageRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class FileController {
    private final ImageRepository imageRepository;

    @Value("${server.image-folder}")
    String imageFolder;

    @GetMapping("/avatars/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) throws IOException {
        File file = new File(imageFolder + filename);
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(new FileInputStream(file)));
    }
}
