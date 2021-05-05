package ru.itis.semwork3.service;

import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.User;

import java.util.Optional;

public interface MessageService {
    Optional<InnerMessageDto> saveNew(String text, Long userId, Long sourceId);

    boolean delete(Long id, User user);
}
