package ru.itis.semwork3.service;

import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.dto.contentsource.TitleSourceDto;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.User;

import java.util.List;
import java.util.Optional;

public interface ContentSourceService {
    List<PreviewSourceDto> findAllByUserId(String id);

    Optional<MainSourceDto> findByIdAndUser(String id, String userId, Pageable pageable);

    Optional<MainSourceDto> saveNew(NewSourceDto dto);

    boolean delete(String id, User user);

    List<TitleSourceDto> searchById(String id);

    Optional<InnerMessageDto> join(String id, String username, String message) throws NotFoundException;

    PreviewSourceDto findPreviewById(String id);
}
