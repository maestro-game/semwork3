package ru.itis.semwork3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.model.ContentSource;
import ru.itis.semwork3.model.Message;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.repository.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentSourceServiceImpl implements ContentSourceService {
    private final Converter<NewSourceDto, ContentSource> toContent;
    private final Converter<ContentSource, MainSourceDto> toMainDto;
    private final ContentSourceRepository contentSourceRepository;
    private final GroupRepository groupRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final DtoRepository dtoRepository;


    @Override
    public List<PreviewSourceDto> findAllByUserId(Long id) {
        return dtoRepository.findAllPreviewSourceDtoByMember(id);
    }

    @Override
    @Transactional
    public Optional<MainSourceDto> findById(Long id) {
        return contentSourceRepository.findById(id).map(contentSource -> {
            // TODO pagination of messages
            return contentSource;
        }).map(toMainDto::convert);
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
                .created(new Timestamp(System.currentTimeMillis()))
                .build()));
        contentSourceRepository.save(contentSource);
        messageRepository.saveAll(contentSource.getMessages());
        return Optional.ofNullable(toMainDto.convert(contentSource));
    }

    @Override
    public boolean delete(Long id, User user) {
        return channelRepository.deleteByIdAndAdmin(id, user) != 0 || groupRepository.deleteByIdAndAdmin(id, user) != 0;
    }
}
