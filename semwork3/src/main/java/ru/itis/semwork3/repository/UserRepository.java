package ru.itis.semwork3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.semwork3.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update User u set u.state = 1, u.confirm = null where u.confirm = :code")
    int confirm(@Param("code") String code);

    @Query(nativeQuery = true, value = "select generate_uid(11)")
    String generateId();
}
