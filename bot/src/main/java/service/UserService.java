package service;


import lombok.extern.slf4j.Slf4j;
import model.entity.AppUser;
import repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


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

    public boolean addUser(String chatId) {
        log.info("Попытка добавить пользователя с chatId = {}", chatId);
        Optional<AppUser> appUser = userRepository.findByChatId(chatId);

        if (appUser.isPresent()) {
            log.info("Пользователь с chatId = {} уже зарегистрирован.", chatId);
            return false;
        } else {
            userRepository.save(new AppUser(chatId));
            log.info("Пользователь с chatId = {} успешно зарегистрирован.", chatId);
            return true;
        }
    }
}
