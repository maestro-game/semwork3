package ru.itis.semwork3.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RemoveMessageDto {
    Long id;
    Timestamp lastMessageTimeStamp;
    String lastMessageShortText;
}
