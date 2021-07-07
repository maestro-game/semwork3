package ru.itis.semwork3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itis.semwork3.model.Channel;
import ru.itis.semwork3.model.User;

public interface ChannelRepository extends JpaRepository<Channel, String> {
    boolean existsByAdminAndId(User admin, String id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from user_source where source_id = 'chch';" +
            "delete from message where source_id = 'chch';" +
            "delete from channel where id = 'chch';" +
            "delete from content_source where id = 'chch';")
    int deleteByIdAndAdmin(String id, User admin);
}
