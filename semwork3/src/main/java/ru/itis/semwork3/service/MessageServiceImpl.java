package ru.itis.semwork3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.ContentSource;
import ru.itis.semwork3.model.Message;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.repository.ContentSourceRepository;
import ru.itis.semwork3.repository.MessageRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ContentSourceRepository sourceRepository;
    private final Converter<Message, InnerMessageDto> converter;

    @Override
    public Optional<InnerMessageDto> saveNew(String text, String userId, String sourceId) {
        var message = messageRepository.save(Message.builder()
                .text(text)
                .author(User.builder().id(userId).build())
                .source(ContentSource.builder().id(sourceId).build())
                .build());
        if (!message.getSource().getMembers().contains(message.getAuthor())) {
            messageRepository.delete(message);
            return Optional.empty();
        }
        return Optional.ofNullable(converter.convert(message));
    }

    @Override
    public Optional<InnerMessageDto> saveNewRepost(Long messageId, String userId, String sourceId) {
        var message = messageRepository.findById(messageId).get();
        var user = User.builder().id(userId).build();
        message = messageRepository.save(Message.builder()
                .text(message.getText())
                .author(user)
                .source(sourceRepository.findByIdAndMembersContaining(sourceId, user).get())
                .build());
        if (!message.getSource().getMembers().contains(message.getAuthor())) {
            messageRepository.delete(message);
            return Optional.empty();
        }
        return Optional.ofNullable(converter.convert(message));
    }

    @Override
    public boolean delete(Long id, User user) {
        return messageRepository.deleteByIdAndAuthor(id, user) == 1;
    }
}
