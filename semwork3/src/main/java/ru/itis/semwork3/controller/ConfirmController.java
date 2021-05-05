package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.semwork3.service.SignUpService;

@Controller
@RequiredArgsConstructor
public class ConfirmController {
    private final SignUpService signUpService;

    @Value("${front.url}") private String FRONT_URL;

    @GetMapping("/confirm/{code}")
    public String confirm(RedirectAttributes redirectAttributes, @PathVariable String code) {
        if (signUpService.confirm(code)) {
            redirectAttributes.addAttribute("info", "Почта успешно подтверждена");
        } else {
            redirectAttributes.addAttribute("error", "Неизвестный код подтверждения");
        }
        return "redirect:" + FRONT_URL + "/signIn";
    }
}
