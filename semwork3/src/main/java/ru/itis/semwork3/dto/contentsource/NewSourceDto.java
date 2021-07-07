package ru.itis.semwork3.dto.contentsource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.semwork3.model.User;

@Getter
public class NewSourceDto {
    String name;
    @Setter String id;
    String about;
    MultipartFile image;
    Integer sourceType;
    Integer type;
    @Setter User admin;
}
