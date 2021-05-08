package ru.itis.semwork3.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.ContentSource;
import ru.itis.semwork3.model.Message;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContentSourceToMainSourceDto implements Converter<ContentSource, MainSourceDto> {
    private final Converter<Message, InnerMessageDto> converter;

    @Override
    public MainSourceDto convert(ContentSource source) {
        return MainSourceDto.builder()
                .id(source.getId())
                .about(source.getAbout())
                .string_id(source.getStringId())
                .messages(source.getMessages().stream().map(converter::convert).collect(Collectors.toList()))
                .name(source.getName())
                .sourceType(source.getSourceType())
                .subs_amount(source.getMembers().size())
                .build();
    }
}
