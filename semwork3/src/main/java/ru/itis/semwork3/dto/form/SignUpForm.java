package ru.itis.semwork3.dto.form;

import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class SignUpForm {
    @Pattern(regexp = "^[-\\w]{0,63}$", message = "Id pattern mismatch")
    public final String id;
    @Size(min = 12, message = "Too short password")
    @Size(max = 63, message = "Too long password")
    public final String password;
    @Email(message = "Email pattern mismatch")
    public final String email;
    @Pattern(regexp = "^[a-zA-Z]{1,63}$", message = "Name pattern mismatch")
    public final String name;
    @Pattern(regexp = "^[a-zA-Z]{1,63}$", message = "Name pattern mismatch")
    public final String surname;
    @Pattern(regexp = "^[\\d]{9,15}$", message = "Surname pattern mismatch")
    public final String phone;
}
