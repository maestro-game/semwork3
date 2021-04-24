package ru.itis.semwork3.util;

public interface EmailUtil {
    void sendMail(String from, String to, String subject, String text);
}
