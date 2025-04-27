package command;

import lombok.extern.slf4j.Slf4j;
import service.SubscriptionServiceInterface;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class UntrackCommand implements Command {

    private final SubscriptionServiceInterface subscriptionService;

    public UntrackCommand(SubscriptionServiceInterface subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText().trim();
        log.info("Обработка команды /untrack для chatId = {} с сообщением '{}'", chatId, message);

        subscriptionService.deleteSubscription(chatId, message.split(" "));
        log.info("Подписка успешно удалена для chatId = {}", chatId);
    }

    @Override
    public String getCommandName() {
        return "/untrack";
    }
}
