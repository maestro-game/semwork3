//package ru.itis.semwork3.dto.converter;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
//import ru.itis.semwork3.model.ContentSource;
//
//@Component
//public class ContentSourceToPreviewSourceDto implements Converter<ContentSource, PreviewSourceDto> {
//    @Override
//    public PreviewSourceDto convert(ContentSource source) {
//        return PreviewSourceDto.builder()
//                .id(source.getId())
//                .name(source.getName())
//                .lastMessageShortText(source.getMessages().get(0).getText())
//                .lastMessageTimestamp(source.getMessages().get(0).getCreated())
//                .build();
//    }
//}
