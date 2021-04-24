package ru.itis.semwork3.dto.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.itis.semwork3.dto.user.ProfileUserDto;

@Getter
@AllArgsConstructor
public class AuthAnswer {
    String token;
    ProfileUserDto user;
}
