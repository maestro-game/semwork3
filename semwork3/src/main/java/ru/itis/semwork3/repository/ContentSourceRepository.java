package ru.itis.semwork3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semwork3.model.ContentSource;

public interface ContentSourceRepository extends JpaRepository<ContentSource, Long> {
}
