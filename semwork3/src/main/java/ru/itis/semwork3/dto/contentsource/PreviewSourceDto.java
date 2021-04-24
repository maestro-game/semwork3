package ru.itis.semwork3.dto.contentsource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@Builder
public class PreviewSourceDto {
    Long id;
    String name;
    String lastMessageShortText;
    Timestamp lastMessageTimestamp;
    @Setter String avatarImageUrl;

    public PreviewSourceDto(Long id, String name, String lastMessageShortText, Timestamp lastMessageTimestamp) {
        this.id = id;
        this.name = name;
        this.lastMessageShortText = lastMessageShortText;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
