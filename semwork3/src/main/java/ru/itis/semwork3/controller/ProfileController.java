package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.prepost.PreAuthorize;
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
import ru.itis.semwork3.security.details.UserDetailsImpl;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    final Converter<User, ProfileUserDto> converter;
    final ImageRepository imageRepository;

//    @GetMapping
//    public String getProfilePage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
//        User user = userDetails.getUser();
//        ProfileUserDto dto = converter.convert(user);
//        //TODO
//        //dto.setPhotoUrl(imageRepository.getById(user.getId()));
//        model.addAttribute("user", dto);
//        return "profile";
//    }

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
