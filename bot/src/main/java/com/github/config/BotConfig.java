package com.github.config;


import com.github.TelegramSender;
import com.github.bot.LinkTrackerBot;
import com.github.command.*;
import com.github.repository.SubscriptionRepository;
import com.github.repository.UserRepository;
import com.github.service.HelpUnknownService;
import com.github.service.SendMessageService;
import com.github.service.SubscriptionService;
import com.github.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("!test")
public class BotConfig {

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        DefaultBotOptions options = new DefaultBotOptions();

        return options;
    }
    @Bean
    public SendMessageService sendMessageService(TelegramSender telegramSender) {
        return new SendMessageService(telegramSender);
    }

    @Bean
    public TelegramSender telegramSender(DefaultBotOptions options) {
        return new TelegramSender(options);
    }

    @Bean
    public UserService userService(UserRepository userRepository, SendMessageService sendMessageService) {
        return new UserService(sendMessageService, userRepository);
    }

    @Bean
    public HelpUnknownService helpUnknownService(SendMessageService sendMessageService) {
        return new HelpUnknownService(sendMessageService);
    }

    @Bean
    public SubscriptionService subscriptionService(SubscriptionRepository subscriptionRepository,
                                                   SendMessageService sendMessageService) {
        return new SubscriptionService(subscriptionRepository, sendMessageService);
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
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        return botsApi;
    }
}
