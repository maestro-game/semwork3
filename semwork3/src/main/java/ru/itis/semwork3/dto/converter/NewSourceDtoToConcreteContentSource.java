package ru.itis.semwork3.dto.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.model.Channel;
import ru.itis.semwork3.model.ContentSource;
import ru.itis.semwork3.model.Group;

@Component
public class NewSourceDtoToConcreteContentSource implements Converter<NewSourceDto, ContentSource> {
    @Override
    public ContentSource convert(NewSourceDto source) {
        switch (source.getSourceType()) {
            case 0 -> {
                return Group.builder()
                        .admin(source.getAdmin())
                        .type(Group.Type.values()[source.getType()])
                        .id(source.getId())
                        .about(source.getAbout())
                        .name(source.getName())
                        .build();
            }
            case 1 -> {
                return Channel.builder()
                        .admin(source.getAdmin())
                        .type(Channel.Type.values()[source.getType()])
                        .id(source.getId())
                        .about(source.getAbout())
                        .name(source.getName())
                        .build();
            }
            default -> throw new IllegalArgumentException("unknown ContentSource type with number " + source.getSourceType());
        }
    }
}
