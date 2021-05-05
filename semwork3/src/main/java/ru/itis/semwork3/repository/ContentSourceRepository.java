package ru.itis.semwork3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semwork3.model.ContentSource;

import java.util.Optional;

public interface ContentSourceRepository extends JpaRepository<ContentSource, Long> {
    Optional<ContentSource> findById(Long id);
}
