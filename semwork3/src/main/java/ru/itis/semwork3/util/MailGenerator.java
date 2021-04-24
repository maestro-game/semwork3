package ru.itis.semwork3.util;

public interface MailGenerator {
    String getConfirmMail(String serverUrl, String confirmCode);
}
