package ru.itis.semwork3.dto.form;

import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class SignInForm {
    @Email
    public final String email;
    @Size(min = 12, message = "Пароль должен быть короче 12 символов")
    @Size(max = 60, message = "Пароль должен быть длиннее 12 символов")
    public final String password;
}
