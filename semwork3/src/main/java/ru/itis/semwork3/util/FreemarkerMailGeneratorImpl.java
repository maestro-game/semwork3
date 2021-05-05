package ru.itis.semwork3.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.model.User;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FreemarkerMailGeneratorImpl implements MailGenerator {
    final Configuration configuration;

    @Override
    public String getConfirmMail(String serverUrl, User user) {
        Template confirmMailTemplate;
        try {
            confirmMailTemplate = configuration.getTemplate("mails/confirm_mail.ftlh");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("server_url", serverUrl);
        attributes.put("user", user);

        StringWriter writer = new StringWriter();
        try {
            confirmMailTemplate.process(attributes, writer);
        } catch (TemplateException | IOException e) {
            throw new IllegalStateException(e);
        }

        return writer.toString();
    }
}
