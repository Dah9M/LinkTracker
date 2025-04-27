package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Service
public class SendMessageService implements SendMessageInterface {

    private final AbsSender sender;

    @Autowired
    public SendMessageService(@Qualifier("linkTrackerBot") AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        log.info("Отправка сообщения пользователю с chatId = {}: {}", chatId, message);

        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.setText(message);
        sm.enableHtml(true);

        try {
            sender.execute(sm);
            log.info("Сообщение успешно отправлено пользователю с chatId = {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение пользователю с chatId = {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    public void sendSubscriptions(String chatId, List<String> subscriptions) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.enableHtml(true);

        if (subscriptions.isEmpty()) {
            log.info("У пользователя с chatId = {} нет активных подписок.", chatId);
            sm.setText("У вас нет активных подписок.");
        } else {
            log.info("Отправляем пользователю с chatId = {} {} подписок.", chatId, subscriptions.size());
            StringBuilder messageBuilder = new StringBuilder("<b>Ваши подписки:</b>\n");
            for (String subscription : subscriptions) {
                messageBuilder.append("- ").append(subscription).append("\n");
            }
            sm.setText(messageBuilder.toString());
        }

        try {
            sender.execute(sm);
            log.info("Список подписок успешно отправлен пользователю с chatId = {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить список подписок пользователю с chatId = {}: {}", chatId, e.getMessage(), e);
        }
    }
}
