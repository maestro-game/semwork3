package ru.itis.semwork3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semwork3.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySource(Long source);
}
