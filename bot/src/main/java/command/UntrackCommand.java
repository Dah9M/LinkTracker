package command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.SubscriptionServiceInterface;

@Slf4j
@Component
public class UntrackCommand implements Command {

    private final SubscriptionServiceInterface subscriptionService;

    public UntrackCommand(SubscriptionServiceInterface subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(Update update, String[] arguments) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        log.info("Обработка команды /untrack для chatId = {} с аргументами '{}'", chatId, String.join(" ", arguments));

        if (arguments.length == 0) {
            log.warn("Пользователь {} не указал ссылку для удаления подписки.", chatId);
            return;
        }

        String[] fullArguments = new String[]{"/untrack", arguments[0]};
        subscriptionService.deleteSubscription(chatId, fullArguments);
        log.info("Подписка успешно удалена для chatId = {}", chatId);
    }

    @Override
    public String getCommandName() {
        return "/untrack";
    }
}