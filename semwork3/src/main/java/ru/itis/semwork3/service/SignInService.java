package ru.itis.semwork3.service;

import ru.itis.semwork3.dto.form.SignInForm;
import ru.itis.semwork3.dto.token.AuthAnswer;
import ru.itis.semwork3.exception.UserIsBannedException;
import ru.itis.semwork3.exception.UserNotFoundException;

public interface SignInService {
    AuthAnswer signIn(SignInForm form) throws UserIsBannedException, UserNotFoundException;
}
