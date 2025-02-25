package com.github.config;


import com.github.bot.LinkTrackerBot;
import com.github.command.CommandContainer;
import com.github.service.SendMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Profile("!test")
public class BotConfig {

    @Bean
    public SendMessageService sendMessageService(TelegramLongPollingBot bot) {
        return new SendMessageService(bot);
    }

    @Bean
    public CommandContainer commandContainer(SendMessageService sendMessageService) {
        return new CommandContainer(sendMessageService);
    }


    @Bean
    public TelegramBotsApi telegramBotsApi(LinkTrackerBot bot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        return botsApi;
    }
}
