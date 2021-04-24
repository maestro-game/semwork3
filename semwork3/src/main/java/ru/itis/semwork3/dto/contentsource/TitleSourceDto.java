package ru.itis.semwork3.dto.contentsource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class TitleSourceDto {
    Long id;
    String name;
    Integer sourceType;
    @Setter String avatarImgUrl;
}
