package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.semwork3.dto.form.SignUpForm;
import ru.itis.semwork3.service.SignUpService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SignUpController {
    private final SignUpService signUpService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpForm form,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    bindingResult
                            .getAllErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining("\n")));
        }
        String error = signUpService.signUp(form);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
        }
        return ResponseEntity.ok().build();
    }
}
