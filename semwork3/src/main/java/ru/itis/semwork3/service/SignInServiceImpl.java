package ru.itis.semwork3.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.semwork3.dto.form.SignInForm;
import ru.itis.semwork3.dto.token.AuthAnswer;
import ru.itis.semwork3.dto.user.ProfileUserDto;
import ru.itis.semwork3.exception.UserIsBannedException;
import ru.itis.semwork3.exception.UserNotFoundException;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final Converter<User, ProfileUserDto> converter;

    @Override
    public AuthAnswer signIn(SignInForm form) throws UserIsBannedException, UserNotFoundException {
        var candidate = userRepository.findByEmail(form.email);
        User user;
        if (candidate.isEmpty() || !passwordEncoder.matches(form.password, (user = candidate.get()).getPassword())) {
            throw new UserNotFoundException("not found");
        }

        if (user.getState() == User.State.NOT_CONFIRMED) {
            throw new UserNotFoundException("confirm");
        }

        if (user.getState() == User.State.BANNED) {
            throw new UserIsBannedException("banned");
        }

        return new AuthAnswer(Jwts.builder()
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact(),
                converter.convert(user));
    }
}
