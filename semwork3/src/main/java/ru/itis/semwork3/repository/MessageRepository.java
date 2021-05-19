package ru.itis.semwork3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.semwork3.model.Message;
import ru.itis.semwork3.model.User;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Transactional
    int deleteByIdAndAuthorAndSource_Id(Long id, User author, String sourceId);

    Page<Message> findAllBySourceId(String id, Pageable pageable);

    Optional<Message> findByIdAndAuthor_Id(Long id, String authorId);
}
