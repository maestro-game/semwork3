package ru.itis.semwork3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.Channel;
import ru.itis.semwork3.model.Message;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.repository.MessageRepository;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final Converter<Message, InnerMessageDto> converter;

    @Override
    public Optional<InnerMessageDto> saveNew(String text, Long userId, Long sourceId) {
        var message = messageRepository.save(Message.builder()
                .text(text)
                .author(User.builder().id(userId).build())
                .created(new Timestamp(System.currentTimeMillis()))
                .source(Channel.builder().id(sourceId).build())
                .build());
        return Optional.ofNullable(converter.convert(message));
    }

    @Override
    public boolean delete(Long id, User user) {
        return messageRepository.deleteByIdAndAuthor(id, user) == 1;
    }
}
