package ru.itis.semwork3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semwork3.model.Group;
import ru.itis.semwork3.model.User;

public interface GroupRepository extends JpaRepository<Group, String> {
    int deleteByIdAndAdmin(String id, User admin);
}
