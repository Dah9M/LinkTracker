package command;

import lombok.extern.slf4j.Slf4j;
import service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import service.SendMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class StartCommand implements Command {

    private final UserService userService;
    private final SendMessageService sendMessageService;

    @Autowired
    public StartCommand(UserService userService, SendMessageService sendMessageService) {
        this.userService = userService;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void execute(Update update, String[] arguments) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        log.info("Обработка команды /start для chatId = {}", chatId);

        boolean isNewUser = userService.addUser(chatId);

        if (isNewUser) {
            log.info("Пользователь с chatId = {} успешно добавлен.", chatId);
            sendMessageService.sendMessage(chatId, "Вы успешно зарегистрированы!");
        } else {
            log.info("Пользователь с chatId = {} уже был зарегистрирован.", chatId);
            sendMessageService.sendMessage(chatId, "Вы уже зарегистрированы.");
        }

        sendMessageService.sendMenu(chatId);
    }

    @Override
    public String getCommandName() {
        return "/start";
    }
}
