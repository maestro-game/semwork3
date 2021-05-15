package ru.itis.semwork3.dto.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.dto.user.ProfileUserDto;
import ru.itis.semwork3.model.User;

@Component
public class UserToProfileUserDto implements Converter<User, ProfileUserDto> {

    @Override
    public ProfileUserDto convert(User user) {
        return new ProfileUserDto(user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhone(),
                user.getAbout());
    }
}
