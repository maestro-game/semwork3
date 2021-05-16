package ru.itis.semwork3.service;

import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.dto.message.RemoveMessageDto;

import java.util.Optional;

public interface MessageService {
    Optional<InnerMessageDto> saveNew(String text, String userId, String sourceId);

    Optional<InnerMessageDto> saveNewRepost(Long messageId, String userId, String sourceId);

    RemoveMessageDto delete(Long id, String userId, String channelId);
}
