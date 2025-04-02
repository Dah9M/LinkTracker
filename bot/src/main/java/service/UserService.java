package service;


import model.entity.AppUser;
import repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static service.MessageName.*;

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
        sendMessageService.sendMessage(chatId, START_MESSAGE.getMessageName());
        Optional<AppUser> appUser = userRepository.findByChatId(chatId);

        if (appUser.isPresent()) {
            sendMessageService.sendMessage(appUser.get().getChatId(), ALREADY_EXIST.getMessageName());
        } else {
            userRepository.save(new AppUser(chatId));

            sendMessageService.sendMessage(chatId, ADD_USER.getMessageName());
        }
    }
}
