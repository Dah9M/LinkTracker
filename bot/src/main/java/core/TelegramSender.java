package core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Slf4j
@Component
public class TelegramSender extends DefaultAbsSender {

    @Getter
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    public TelegramSender(DefaultBotOptions dbo) {
        super(dbo);
        log.info("Инициализирован отправщик сообщений Telegram.");
    }
}
