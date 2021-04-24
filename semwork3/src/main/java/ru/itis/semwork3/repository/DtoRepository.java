package ru.itis.semwork3.repository;

import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;

import java.util.List;

public interface DtoRepository {
    List<PreviewSourceDto> findAllPreviewSourceDtoByMember(Long id);
}
