package ru.itis.semwork3.dto.contentsource;

import lombok.Builder;
import lombok.Getter;
import ru.itis.semwork3.dto.message.InnerMessageDto;

import java.util.List;

@Getter
@Builder
public class MainSourceDto {
    Long id;
    String string_id;
    String name;
    Integer sourceType;
    List<InnerMessageDto> messages;
    String avatarImageUrl;
}
