package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SendMessageService(AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.setText(message);
        sm.enableHtml(true);

        try {
            sender.execute(sm);
        } catch (TelegramApiException e) {
            //log.error("Failed to send message {} via Telegram API.", message);
            e.printStackTrace();
        }
    }

    @Override
    public void sendSubscriptions(String chatId, List<String> subscriptions) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.enableHtml(true);
    }
}
