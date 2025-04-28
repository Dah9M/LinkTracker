package bot;

import command.CommandContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.SendMessageInterface;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LinkTrackerBot extends TelegramLongPollingBot {

    private static final String COMMAND_PREFIX = "/";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final CommandContainer commandContainer;
    private final SendMessageInterface sendMessageService;
    private final Map<String, BotState> userStates = new HashMap<>();

    private static final Map<String, String> buttonCommandMap = Map.of(
            "Добавить подписку", "/track",
            "Удалить подписку", "/untrack",
            "Мои подписки", "/list"
    );

    @Autowired
    public LinkTrackerBot(CommandContainer commandContainer, SendMessageInterface sendMessageService) {
        this.commandContainer = commandContainer;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText().trim();

            log.info("Получено сообщение от chatId = {}: {}", chatId, message);

            BotState userState = userStates.getOrDefault(chatId, BotState.IDLE);

            try {
                switch (userState) {
                    case AWAITING_TRACK_URL -> {
                        log.info("Пользователь {} в состоянии AWAITING_TRACK_URL. Обработка ссылки.", chatId);
                        commandContainer.retrieveCommand("/track").execute(update, new String[]{message});
                        userStates.put(chatId, BotState.IDLE);
                        return;
                    }
                    case AWAITING_UNTRACK_URL -> {
                        log.info("Пользователь {} в состоянии AWAITING_UNTRACK_URL. Обработка ссылки.", chatId);
                        commandContainer.retrieveCommand("/untrack").execute(update, new String[]{message});
                        userStates.put(chatId, BotState.IDLE);
                        return;
                    }
                    case IDLE -> {
                    }
                }

                if (buttonCommandMap.containsKey(message)) {
                    String command = buttonCommandMap.get(message);

                    switch (command) {
                        case "/track" -> {
                            sendMessageService.sendSimpleMessage(chatId, "Пожалуйста, отправьте ссылку, которую хотите добавить в подписки.");
                            userStates.put(chatId, BotState.AWAITING_TRACK_URL);
                            return;
                        }
                        case "/untrack" -> {
                            sendMessageService.sendSimpleMessage(chatId, "Пожалуйста, отправьте ссылку, которую хотите удалить из подписок.");
                            userStates.put(chatId, BotState.AWAITING_UNTRACK_URL);
                            return;
                        }
                        case "/list" -> {
                            commandContainer.retrieveCommand(command).execute(update, new String[]{});
                            return;
                        }
                    }
                } else if (message.startsWith(COMMAND_PREFIX)) {
                    String commandIdentifier = message.split(" ")[0].toLowerCase();
                    log.info("Обнаружена команда: {}", commandIdentifier);
                    commandContainer.retrieveCommand(commandIdentifier).execute(update, new String[]{});
                } else {
                    log.info("Получено обычное сообщение, передача на команду 'NO'.");
                    commandContainer.retrieveCommand("NO").execute(update, new String[]{});
                }

            } catch (TelegramApiException e) {
                log.error("Ошибка при выполнении команды: {}", e.getMessage(), e);
            }
        }
    }
}
