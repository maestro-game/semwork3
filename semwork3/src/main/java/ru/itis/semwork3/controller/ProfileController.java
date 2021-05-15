package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ResponseEntity<ProfileUserDto> getProfilePage(@AuthenticationPrincipal UserDetails userDetails) {
        return userRepository.findById(userDetails.getUsername())
                .map(converter::convert)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public String importQuestion(@RequestParam("file") MultipartFile multipart,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (multipart == null || multipart.isEmpty()) {
            //TODO
            return "error";
        }
        if (multipart.getSize() / 1024 / 1024 >= 4) {
            //TODO
            return "error";
        }
        String ext = multipart.getOriginalFilename().substring(multipart.getOriginalFilename().lastIndexOf('.'));
        if (!ext.equals("jpg") && !ext.equals("jpeg") && !ext.equals("png") && !ext.equals("bmp")) {
            //TODO
            return "error";
        }
        try {
            imageRepository.save(multipart.getInputStream(), Long.valueOf(userDetails.getUsername()));
        } catch (IOException e) {
            //TODO
            return "error";
        }
        //TODO
        return "ok";
    }
}
