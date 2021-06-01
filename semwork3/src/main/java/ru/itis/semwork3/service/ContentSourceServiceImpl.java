package ru.itis.semwork3.service;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.dto.contentsource.TitleSourceDto;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.ContentSource;
import ru.itis.semwork3.model.Group;
import ru.itis.semwork3.model.Message;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentSourceServiceImpl implements ContentSourceService {
    private final static String JOIN_MESSAGE_TEXT = "Присоединился пользователь ";
    private final Converter<NewSourceDto, ContentSource> toContent;
    private final Converter<ContentSource, MainSourceDto> toMainDto;
    private final Converter<Message, InnerMessageDto> toInnerMessage;
    private final Converter<ContentSource, TitleSourceDto> toTitleSource;
    private final ContentSourceRepository contentSourceRepository;
    private final GroupRepository groupRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final DtoRepository dtoRepository;
    private final ImageRepository imageRepository;

    private final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";


    @Override
    public List<PreviewSourceDto> findAllByUserId(String id) {
        var sources = dtoRepository.findAllPreviewSourceDtoByMember(id);
        sources.forEach(dto -> {
            dto.setAvatarImageUrl(imageRepository.get(dto.getId()));
        });
        return sources;
    }

    @Override
    @Transactional
    public Optional<MainSourceDto> findByIdAndUser(String id, String userId, Pageable pageable) {
        var source = contentSourceRepository.findByIdAndMembersContaining(id, User.builder().id(userId).build());
        if (source.isPresent() || (source = contentSourceRepository.findById(id)).isPresent() && source.get().getTypeNumber() == 0) {
            return source.map(toMainDto::convert).map(mainSourceDto -> {
                mainSourceDto.setMessages(messageRepository
                        .findAllBySourceId(mainSourceDto.getId(), pageable)
                        .map(toInnerMessage::convert));
                mainSourceDto.setAvatarImageUrl(imageRepository.get(mainSourceDto.getId()));
                return mainSourceDto;
            });
        }
        return Optional.empty();
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
        result.setAvatarImageUrl(imageRepository.get(result.getId()));
        return Optional.of(result);
    }

    @Override
    public boolean delete(String id, User user) {
        return channelRepository.deleteByIdAndAdmin(id, user) != 0 || groupRepository.deleteByIdAndAdmin(id, user) != 0;
    }

    @Override
    public List<TitleSourceDto> searchById(String id) {
        var sources = contentSourceRepository.findByIdContains(id, PageRequest.of(0, 10, Sort.by("id").ascending()))
                .stream()
                .map(toTitleSource::convert)
                .collect(Collectors.toList());
        sources.forEach(dto -> {
            dto.setAvatarImageUrl(imageRepository.get(dto.getId()));
        });
        return sources;
    }

    @Override
    @Transactional
    public Optional<InnerMessageDto> join(String id, String username, @Nullable String message) throws NotFoundException {
        var candidate = contentSourceRepository.findById(id);
        if (candidate.isPresent()) {
            var source = candidate.get();
            if (source.getTypeNumber() == 0) {
                switch (source.getSourceType()) {
                    case 0 -> {
                        contentSourceRepository.addMember(source.getId(), username);
                        return Optional.of(messageRepository.save(Message.builder()
                                .text("Присоединился пользователь " + username)
                                .author(null)
                                .source(source)
                                .from(null)
                                .build())).map(toInnerMessage::convert);
                    }
                    case 1 -> {
                        contentSourceRepository.addMember(source.getId(), username);
                        return Optional.empty();
                    }
                    case 2 -> {
                        String genId = id + username;
                        genId = (genId + BASE64.charAt(id.length() - 1) + "________________________________").substring(0, Math.max(32, genId.length() + 1));
                        var newGroup = contentSourceRepository.save(Group.builder()
                                .admin(null)
                                .type(Group.Type.PRIVATE)
                                .id(genId)
                                .about(null)
                                .name(null)
                                .build());
                        contentSourceRepository.addMember(genId, username);
                        contentSourceRepository.addMember(genId, id);
                        messageRepository.saveAll(List.of(
                                Message.builder()
                                        .text("Начало общения")
                                        .author(null)
                                        .source(newGroup)
                                        .from(null)
                                        .build(),
                                Message.builder()
                                        .text(message)
                                        .author(User.builder().id(username).build())
                                        .source(newGroup)
                                        .from(null)
                                        .build()
                        ));
                        return Optional.of(InnerMessageDto.builder().text(genId).build());
                    }
                }
            }
        }
        throw new NotFoundException("channel not found");
    }

    @Override
    public PreviewSourceDto findPreviewById(String id) {
        var source = dtoRepository.findPreviewSourceDtoById(id);
        source.setAvatarImageUrl(imageRepository.get(source.getId()));
        return source;
    }
}
