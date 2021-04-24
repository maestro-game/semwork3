package ru.itis.semwork3.service;

import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.model.User;

import java.util.List;
import java.util.Optional;

public interface ContentSourceService {
    List<PreviewSourceDto> findAllByUserId(Long id);

    Optional<MainSourceDto> findById(Long id);

    Optional<MainSourceDto> saveNew(NewSourceDto dto);

    boolean delete(Long id, User user);
}
