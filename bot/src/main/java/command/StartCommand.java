package command;

import lombok.extern.slf4j.Slf4j;
import service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class StartCommand implements Command {

    private final UserService userService;

    public StartCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void execute(Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        log.info("Обработка команды /start для пользователя с chatId = {}", chatId);
        userService.addUser(chatId);
        log.info("Пользователь с chatId = {} успешно добавлен.", chatId);
    }

    @Override
    public String getCommandName() {
        return "/start";
    }
}
