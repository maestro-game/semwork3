package ru.itis.semwork3.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class ProfileUserDto {
    final Long id;
    final String string_id;
    final String name;
    final String surname;
    final String email;
    final String phone;
    final String about;
    @Setter String photoUrl;
}
