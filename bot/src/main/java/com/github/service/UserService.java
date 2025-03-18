package com.github.service;


import com.github.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.service.MessageName.*;

@Service
public class UserService {

    private final SendMessageInterface sendMessageService;
    private final ChatUserRepository userRepository;

    @Autowired
    public UserService(SendMessageInterface sendMessageService, ChatUserRepository userRepository) {
        this.sendMessageService = sendMessageService;
        this.userRepository = userRepository;
    }

    public void addUser(String chatId) throws TelegramApiException {
        sendMessageService.sendMessage(chatId, START_MESSAGE.getMessageName());

        Long userStatus = userRepository.createUser(chatId);

        sendMessageService.sendMessage(chatId, ADD_USER.getMessageName());
    }
}
