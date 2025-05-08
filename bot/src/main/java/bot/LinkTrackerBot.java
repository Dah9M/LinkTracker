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
import service.SubscriptionServiceInterface;

import java.util.HashMap;
import java.util.List;
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
    private final SubscriptionServiceInterface subscriptionService;
    private final Map<String, BotState> userStates = new HashMap<>();
    private final Map<String, String> pendingUntrackUrl = new HashMap<>();

    private static final Map<String, String> buttonCommandMap = Map.of(
            "Добавить подписку", "/track",
            "Удалить подписку", "/untrack",
            "Мои подписки", "/list"
    );

    @Autowired
    public LinkTrackerBot(CommandContainer commandContainer,
                          SendMessageInterface sendMessageService,
                          SubscriptionServiceInterface subscriptionService) {
        this.commandContainer = commandContainer;
        this.sendMessageService = sendMessageService;
        this.subscriptionService = subscriptionService;
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
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText().trim();
        log.info("Получено сообщение от chatId = {}: {}", chatId, message);

        BotState state = userStates.getOrDefault(chatId, BotState.IDLE);

        try {
            switch (state) {
                case AWAITING_TRACK_URL -> {
                    log.info("Пользователь {} в состоянии AWAITING_TRACK_URL. Обработка ссылки.", chatId);
                    commandContainer.retrieveCommand("/track")
                            .execute(update, new String[]{message});
                    userStates.put(chatId, BotState.IDLE);
                    return;
                }
                case AWAITING_UNTRACK_URL -> {
                    log.info("Пользователь {} в состоянии AWAITING_UNTRACK_URL. Обработка номера '{}'.", chatId, message);
                    try {
                        int idx = Integer.parseInt(message);
                        List<String> urls = subscriptionService.getSubscriptionUrls(chatId);
                        if (idx < 1 || idx > urls.size()) {
                            sendMessageService.sendSimpleMessage(chatId,
                                    "Неверный номер: введите от 1 до " + urls.size());
                            log.warn("Неверный индекс {} для chatId = {}", idx, chatId);
                            return;
                        }
                        String url = urls.get(idx - 1);
                        pendingUntrackUrl.put(chatId, url);
                        sendMessageService.sendSimpleMessage(chatId,
                                "Вы точно хотите отписаться от:\n" + url + "\nНапишите «Да» или «Нет».");
                        log.info("Запрошено подтверждение удаления ссылки '{}' для {}", url, chatId);
                        userStates.put(chatId, BotState.AWAITING_UNTRACK_CONFIRMATION);
                    } catch (NumberFormatException ex) {
                        sendMessageService.sendSimpleMessage(chatId, "Пожалуйста, введите номер ссылки.");
                        log.warn("Некорректный ввод числа '{}' для chatId = {}", message, chatId);
                    }
                    return;
                }
                case AWAITING_UNTRACK_CONFIRMATION -> {
                    log.info("Пользователь {} подтвердил операцию: '{}'.", chatId, message);
                    if (message.equalsIgnoreCase("да")) {
                        String url = pendingUntrackUrl.remove(chatId);
                        commandContainer.retrieveCommand("/untrack")
                                .execute(update, new String[]{url});
                        log.info("Выполняется команда /untrack для chatId = {}, url = {}", chatId, url);
                    } else {
                        sendMessageService.sendSimpleMessage(chatId, "Отмена удаления.");
                        log.info("Пользователь {} отменил удаление подписки.", chatId);
                    }
                    userStates.put(chatId, BotState.IDLE);
                    return;
                }
                case IDLE -> {}
            }

            if (buttonCommandMap.containsKey(message)) {
                String cmd = buttonCommandMap.get(message);
                switch (cmd) {
                    case "/track" -> {
                        log.info("Кнопка 'Добавить подписку' нажата пользователем {}", chatId);
                        sendMessageService.sendSimpleMessage(chatId,
                                "Пожалуйста, отправьте ссылку, которую хотите добавить в подписки.");
                        userStates.put(chatId, BotState.AWAITING_TRACK_URL);
                        return;
                    }
                    case "/untrack" -> {
                        log.info("Кнопка 'Удалить подписку' нажата пользователем {}", chatId);
                        subscriptionService.getSubscriptions(chatId);
                        sendMessageService.sendSimpleMessage(chatId,
                                "Введите номер подписки, которую хотите удалить:");
                        userStates.put(chatId, BotState.AWAITING_UNTRACK_URL);
                        return;
                    }
                    case "/list" -> {
                        log.info("Кнопка 'Мои подписки' нажата пользователем {}", chatId);
                        commandContainer.retrieveCommand("/list").execute(update, new String[]{});
                        return;
                    }
                }
            } else if (message.startsWith(COMMAND_PREFIX)) {
                String identifier = message.split(" ")[0].toLowerCase();
                log.info("Обнаружена команда: {} от {}", identifier, chatId);
                commandContainer.retrieveCommand(identifier).execute(update, new String[]{});
            } else {
                log.info("Получено обычное сообщение, передача на команду NO от {}", chatId);
                commandContainer.retrieveCommand("NO").execute(update, new String[]{});
            }

        } catch (TelegramApiException e) {
            log.error("Ошибка в onUpdateReceived: ", e);
        }
    }
}