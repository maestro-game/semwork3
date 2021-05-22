package ru.itis.semwork3.repository;

import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.dto.message.RemoveMessageDto;

import java.util.List;

public interface DtoRepository {
    List<PreviewSourceDto> findAllPreviewSourceDtoByMember(String id);
    RemoveMessageDto findRemoveMessageDtoBySourceIdAndMessageId(String sourceId, Long messageId);

    PreviewSourceDto findPreviewSourceDtoById(String id);
}
