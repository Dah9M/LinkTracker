package core;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class TelegramSender extends DefaultAbsSender {

    @Getter
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    public TelegramSender(DefaultBotOptions dbo) {
        super(dbo);
    }
}
