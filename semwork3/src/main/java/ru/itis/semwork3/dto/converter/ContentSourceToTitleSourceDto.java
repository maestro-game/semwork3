package ru.itis.semwork3.dto.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.dto.contentsource.TitleSourceDto;
import ru.itis.semwork3.model.ContentSource;

@Component
public class ContentSourceToTitleSourceDto implements Converter<ContentSource, TitleSourceDto> {

    @Override
    public TitleSourceDto convert(ContentSource source) {
        return TitleSourceDto.builder()
                .id(source.getId())
                .name(source.getName())
                .sourceType(source.getSourceType())
                .build();
    }
}
