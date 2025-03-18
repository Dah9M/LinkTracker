package com.github.repository;

import com.github.entity.ChatUserEntity;
import com.github.model.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ChatUserRepository {

    private final ChatUserSpringDataRepository repository;

    @Autowired
    public ChatUserRepository(ChatUserSpringDataRepository jpaRepo) {
        this.repository = jpaRepo;
    }

    public Long createUser(String chatId) {
        ChatUserEntity entity = new ChatUserEntity();
        entity.setChatId(chatId);
        entity.setRegisteredAt(LocalDateTime.now());

        ChatUserEntity saved = repository.save(entity);
        return saved.getId();
    }

    public ChatUser findByChatId(String chatId) {
        ChatUserEntity entity = repository.findByChatId(chatId);
        return entity == null ? null : toModel(entity);
    }

    public int deleteByChatId(String chatId) {
        ChatUserEntity entity = repository.findByChatId(chatId);
        if (entity == null) {
            return 0;
        }
        repository.delete(entity);
        return 1;
    }

    public List<ChatUser> findAll() {
        return repository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    private ChatUser toModel(ChatUserEntity entity) {
        return new ChatUser(
                entity.getId(),
                entity.getChatId(),
                entity.getRegisteredAt()
        );
    }
}
