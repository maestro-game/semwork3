package ru.itis.semwork3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.semwork3.model.Message;
import ru.itis.semwork3.model.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
    int deleteByIdAndAuthor(Long id, User author);

    Page<Message> findAllBySourceId(String id, Pageable pageable);
}
