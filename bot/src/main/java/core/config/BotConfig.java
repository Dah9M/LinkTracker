package core.config;

import command.*;
import core.TelegramSender;
import bot.LinkTrackerBot;

import lombok.extern.slf4j.Slf4j;
import service.HelpUnknownService;
import service.SendMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class BotConfig {

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        DefaultBotOptions options = new DefaultBotOptions();
        log.info("Создан DefaultBotOptions.");
        return options;
    }
    @Bean
    public SendMessageService sendMessageService(TelegramSender telegramSender) {
        log.info("Создан SendMessageService.");
        return new SendMessageService(telegramSender);
    }

    @Bean
    public TelegramSender telegramSender(DefaultBotOptions options) {
        log.info("Создан TelegramSender.");
        return new TelegramSender(options);
    }


    @Bean
    public HelpUnknownService helpUnknownService(SendMessageService sendMessageService) {
        log.info("Создан HelpUnknownService.");
        return new HelpUnknownService(sendMessageService);
    }

    @Bean
    public CommandContainer commandContainer(List<Command> commandList,
                                             HelpCommand helpCommand,
                                             ListCommand listCommand,
                                             StartCommand startCommand,
                                             TrackCommand trackCommand,
                                             UntrackCommand untrackCommand,
                                             UnknownCommand unknownCommand,
                                             NoCommand noCommand
                                             ) {
        log.info("Создан CommandContainer.");

        List<Command> commands = new ArrayList<>(commandList);
        commands.add(helpCommand);
        commands.add(listCommand);
        commands.add(startCommand);
        commands.add(trackCommand);
        commands.add(unknownCommand);
        commands.add(untrackCommand);
        commands.add(noCommand);
        return new CommandContainer(commandList);
    }


    @Bean
    public TelegramBotsApi telegramBotsApi(LinkTrackerBot bot) throws TelegramApiException {
        log.info("Создан и зарегистрирован TelegramBotsApi.");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        return botsApi;
    }
}
