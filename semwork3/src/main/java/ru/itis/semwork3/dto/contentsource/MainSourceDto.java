package ru.itis.semwork3.dto.contentsource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import ru.itis.semwork3.dto.message.InnerMessageDto;

@Getter
@Builder
public class MainSourceDto {
    String about;
    String id;
    String name;
    Integer sourceType;
    @Setter Page<InnerMessageDto> messages;
    Integer subsAmount;
    @Setter String avatarImageUrl;
}
