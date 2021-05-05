package ru.itis.semwork3.util;

import ru.itis.semwork3.model.User;

public interface MailGenerator {
    String getConfirmMail(String serverUrl, User user);
}
