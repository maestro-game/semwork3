package ru.itis.semwork3.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.model.ContentSource;

@Component
@RequiredArgsConstructor
public class ContentSourceToMainSourceDto implements Converter<ContentSource, MainSourceDto> {

    @Override
    public MainSourceDto convert(ContentSource source) {
        return MainSourceDto.builder()
                .about(source.getAbout())
                .id(source.getId())
                .name(source.getName())
                .sourceType(source.getSourceType())
                .subsAmount(source.getMembers().size())
                .build();
    }
}
