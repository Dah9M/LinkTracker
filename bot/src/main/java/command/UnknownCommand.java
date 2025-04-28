package command;

import lombok.extern.slf4j.Slf4j;
import service.HelpUnknownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class UnknownCommand implements Command {

    private final HelpUnknownService helpUnknownService;

    @Autowired
    public UnknownCommand(HelpUnknownService helpUnknownService) {
        this.helpUnknownService = helpUnknownService;
    }


    @Override
    public void execute(Update update, String[] arguments) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        log.warn("Неизвестная команда от пользователя с chatId = {}", chatId);

        helpUnknownService.sendUnknownText(chatId);
        log.info("Ответ на неизвестную команду отправлен пользователю с chatId = {}", chatId);
    }

    @Override
    public String getCommandName() {
        return "UNKNOWN";
    }
}
