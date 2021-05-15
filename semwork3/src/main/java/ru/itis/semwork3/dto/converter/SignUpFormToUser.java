package ru.itis.semwork3.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.dto.form.SignUpForm;
import ru.itis.semwork3.model.User;

@Component
@RequiredArgsConstructor
public class SignUpFormToUser implements Converter<SignUpForm, User> {
    final PasswordEncoder passwordEncoder;
    
    @Override
    public User convert(SignUpForm form) {
        return User.builder()
                .id(form.id)
                .password(passwordEncoder.encode(form.password))
                .email(form.email)
                .phone(form.phone)
                .name(form.name)
                .surname(form.surname)
                .build();
    }
}
