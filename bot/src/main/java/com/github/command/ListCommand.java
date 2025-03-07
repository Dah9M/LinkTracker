package com.github.command;

import com.github.service.SendMessageInterface;
import com.github.service.SubscriptionServiceInterface;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ListCommand implements Command {

    private final SubscriptionServiceInterface subscriptionService;

    public ListCommand(SubscriptionServiceInterface subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();

        subscriptionService.getSubscriptions(chatId);
    }

    @Override
    public String getCommandName() {
        return "/list";
    }
}
