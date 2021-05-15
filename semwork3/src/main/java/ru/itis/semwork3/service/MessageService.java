package ru.itis.semwork3.service;

import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.User;

import java.util.Optional;

public interface MessageService {
    Optional<InnerMessageDto> saveNew(String text, String userId, String sourceId);

    Optional<InnerMessageDto> saveNewRepost(Long messageId, String userId, String sourceId);

    boolean delete(Long id, User user);
}
