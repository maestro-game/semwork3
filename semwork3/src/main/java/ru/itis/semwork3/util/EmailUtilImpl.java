package ru.itis.semwork3.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class EmailUtilImpl implements EmailUtil {
    final private JavaMailSender mailSender;
    final private ExecutorService executorService;

    @Override
    public void sendMail(String from, String to, String subject, String text) {
        executorService.submit(() -> mailSender.send(mimeMessage -> {
            var messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
        }));
    }
}
