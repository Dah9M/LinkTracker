package com.github.service;


import com.github.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.service.MessageName.*;

@Service
public class UserService {

    private final SendMessageInterface sendMessageService;
    private final UserRepository userRepository;

    @Autowired
    public UserService(SendMessageInterface sendMessageService, UserRepository userRepository) {
        this.sendMessageService = sendMessageService;
        this.userRepository = userRepository;
    }

    public void addUser(String chatId) throws TelegramApiException {
        sendMessageService.sendMessage(chatId, START_MESSAGE.getMessageName());

        String userStatus = userRepository.addUser(chatId);

        if (userStatus.equals("ALREADY_EXIST")) {
            sendMessageService.sendMessage(chatId, ALREADY_EXIST.getMessageName());
            return;
        }

        sendMessageService.sendMessage(chatId, ADD_USER.getMessageName());
    }
}
