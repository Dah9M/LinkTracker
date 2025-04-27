package command;

import lombok.extern.slf4j.Slf4j;
import service.HelpUnknownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class HelpCommand implements Command {

    private final HelpUnknownService helpUnknownService;

    @Autowired
    public HelpCommand(HelpUnknownService helpUnknownService) {
        this.helpUnknownService = helpUnknownService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        log.info("Обработка команды /help для пользователя с chatId = {}", chatId);

        helpUnknownService.sendHelpText(chatId);
        log.info("Справка успешно отправлена пользователю с chatId = {}", chatId);
    }

    @Override
    public String getCommandName() {
        return "/help";
    }
}
