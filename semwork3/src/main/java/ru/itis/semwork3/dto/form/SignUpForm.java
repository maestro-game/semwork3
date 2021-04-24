package ru.itis.semwork3.dto.form;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignUpForm {
    public final String id;
    public final String password;
    public final String email;
    public final String name;
    public final String surname;
    public final String phone;


}