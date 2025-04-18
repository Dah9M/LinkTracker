package command;

import service.SubscriptionServiceInterface;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

        subscriptionService.deleteSubscription(chatId, message.split(" "));
    }

    @Override
    public String getCommandName() {
        return "/untrack";
    }
}
