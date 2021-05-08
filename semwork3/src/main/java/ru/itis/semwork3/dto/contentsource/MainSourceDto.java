package ru.itis.semwork3.dto.contentsource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.itis.semwork3.dto.message.InnerMessageDto;

import java.util.List;

@Getter
@Builder
public class MainSourceDto {
    Long id;
    String about;
    String string_id;
    String name;
    Integer sourceType;
    @Setter List<InnerMessageDto> messages;
    Integer subs_amount;
    String avatarImageUrl;
}
