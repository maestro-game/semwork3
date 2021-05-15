package ru.itis.semwork3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.ContentSource;
import ru.itis.semwork3.model.Message;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentSourceServiceImpl implements ContentSourceService {
    private final Converter<NewSourceDto, ContentSource> toContent;
    private final Converter<ContentSource, MainSourceDto> toMainDto;
    private final Converter<Message, InnerMessageDto> toInnerMessage;
    private final ContentSourceRepository contentSourceRepository;
    private final GroupRepository groupRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final DtoRepository dtoRepository;


    @Override
    public List<PreviewSourceDto> findAllByUserId(String id) {
        return dtoRepository.findAllPreviewSourceDtoByMember(id);
    }

    @Override
    @Transactional
    public Optional<MainSourceDto> findByIdAndUser(String id, String userId, Pageable pageable) {
        return contentSourceRepository.findByIdAndMembersContaining(id, User.builder().id(userId).build()).map(toMainDto::convert).map(mainSourceDto -> {
            mainSourceDto.setMessages(messageRepository
                    .findAllBySourceId(mainSourceDto.getId(), pageable)
                    .map(toInnerMessage::convert));
            return mainSourceDto;
        });
    }

    @Override
    public Optional<MainSourceDto> saveNew(NewSourceDto dto) {
        ContentSource contentSource = toContent.convert(dto);
        contentSource.setMembers(Collections.singletonList(dto.getAdmin()));
        contentSource.setMessages(Collections.singletonList(Message.builder()
                .text(dto.getSourceType() == 1 ? "Канал создан" : "Группа создана")
                .author(null)
                .source(contentSource)
                .from(null)
                .build()));
        contentSourceRepository.save(contentSource);
        messageRepository.saveAll(contentSource.getMessages());
        var result = toMainDto.convert(contentSource);
        result.setMessages(messageRepository
                .findAllBySourceId(result.getId(), PageRequest.of(0, 20, Sort.by("id").ascending()))
                .map(toInnerMessage::convert));
        return Optional.ofNullable(result);
    }

    @Override
    public boolean delete(String id, User user) {
        return channelRepository.deleteByIdAndAdmin(id, user) != 0 || groupRepository.deleteByIdAndAdmin(id, user) != 0;
    }
}
