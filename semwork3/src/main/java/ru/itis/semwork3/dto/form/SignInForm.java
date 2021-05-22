package ru.itis.semwork3.dto.form;

import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class SignInForm {
    @Email(message = "Email pattern mismatch")
    public final String email;
    @Size(min = 12, message = "Too short password")
    @Size(max = 63, message = "Too long password")
    public final String password;
}
