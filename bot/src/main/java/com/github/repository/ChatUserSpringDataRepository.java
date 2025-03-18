package com.github.repository;

import com.github.entity.ChatUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatUserSpringDataRepository extends JpaRepository<ChatUserEntity, Long> {
    ChatUserEntity findByChatId(String chatId);
}
