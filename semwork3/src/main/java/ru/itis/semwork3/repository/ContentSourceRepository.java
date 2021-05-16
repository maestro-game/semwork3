package ru.itis.semwork3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semwork3.model.ContentSource;
import ru.itis.semwork3.model.User;

import java.util.Optional;

public interface ContentSourceRepository extends JpaRepository<ContentSource, String> {
    Optional<ContentSource> findByIdAndMembersContaining(String id, User user);

    boolean existsByMembersContainsAndId(User user, String id);
}
