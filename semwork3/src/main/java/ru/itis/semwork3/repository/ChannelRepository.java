package ru.itis.semwork3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semwork3.model.Channel;
import ru.itis.semwork3.model.User;

public interface ChannelRepository extends JpaRepository<Channel, String> {
    int deleteByIdAndAdmin(String id, User admin);
}
