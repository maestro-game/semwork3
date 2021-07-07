package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.semwork3.dto.form.SignInForm;
import ru.itis.semwork3.exception.UserIsBannedException;
import ru.itis.semwork3.exception.UserNotFoundException;
import ru.itis.semwork3.repository.ImageRepository;
import ru.itis.semwork3.service.SignInService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signIn")
public class SignInController {
    private final SignInService signInService;
    private final ImageRepository imageRepository;

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInForm signInForm) {
        try {
            var authAnswer = signInService.signIn(signInForm);
            authAnswer.getUser().setPhotoUrl(imageRepository.get(authAnswer.getUser().getId()));
            return ResponseEntity.ok(authAnswer);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserIsBannedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
