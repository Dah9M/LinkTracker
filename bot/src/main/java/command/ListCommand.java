package command;

import lombok.extern.slf4j.Slf4j;
import service.SubscriptionServiceInterface;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class ListCommand implements Command {

    private final SubscriptionServiceInterface subscriptionService;

    public ListCommand(SubscriptionServiceInterface subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        log.info("Обработка команды /list для пользователя с chatId = {}", chatId);

        subscriptionService.getSubscriptions(chatId);
        log.info("Список подписок успешно отправлен пользователю с chatId = {}", chatId);
    }

    @Override
    public String getCommandName() {
        return "/list";
    }
}
