package service;


import lombok.extern.slf4j.Slf4j;
import model.entity.AppUser;
import repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static service.MessageName.*;

@Slf4j
@Service
public class UserService {

    private final SendMessageInterface sendMessageService;
    private final AppUserRepository userRepository;

    @Autowired
    public UserService(SendMessageInterface sendMessageService, AppUserRepository userRepository) {
        this.sendMessageService = sendMessageService;
        this.userRepository = userRepository;
    }

    public void addUser(String chatId) throws TelegramApiException {
        log.info("Попытка добавить пользователя с chatId = {}", chatId);
        sendMessageService.sendMessage(chatId, START_MESSAGE.getMessageName());
        Optional<AppUser> appUser = userRepository.findByChatId(chatId);

        if (appUser.isPresent()) {
            log.info("Пользователь с chatId = {} уже зарегистрирован.", chatId);
            sendMessageService.sendMessage(appUser.get().getChatId(), ALREADY_EXIST.getMessageName());
        } else {
            userRepository.save(new AppUser(chatId));
            log.info("Пользователь с chatId = {} успешно добавлен.", chatId);
            sendMessageService.sendMessage(chatId, ADD_USER.getMessageName());
        }
    }
}
