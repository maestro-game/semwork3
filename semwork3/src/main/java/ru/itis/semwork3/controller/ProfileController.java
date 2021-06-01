package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.semwork3.dto.user.ProfileUserDto;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.repository.ImageRepository;
import ru.itis.semwork3.repository.UserRepository;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final Converter<User, ProfileUserDto> converter;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ProfileUserDto> getProfileEntity(@AuthenticationPrincipal UserDetails userDetails) {
        return userRepository.findById(userDetails.getUsername())
                .map(converter::convert)
                .map(a -> {
                    a.setPhotoUrl(imageRepository.get(a.getId()));
                    return a;
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> importQuestion(@RequestParam("file") MultipartFile multipart,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (multipart == null || multipart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String ext = multipart.getOriginalFilename().substring(multipart.getOriginalFilename().lastIndexOf('.') + 1);
        if (!ext.equals("jpg") && !ext.equals("jpeg") && !ext.equals("png") && !ext.equals("bmp")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            return ResponseEntity.ok("\"" + imageRepository.save(multipart, userDetails.getUsername()) + "\"");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }
}
