package ru.itis.semwork3.service;

import ru.itis.semwork3.dto.form.SignUpForm;

public interface SignUpService {
    String signUp(SignUpForm form);
    boolean confirm(String code);
}
