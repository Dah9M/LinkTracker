package bot;

import command.CommandContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class LinkTrackerBot extends TelegramLongPollingBot {

    private final static String COMMAND_PREFIX = "/";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final CommandContainer commandContainer;

    public LinkTrackerBot(@Autowired CommandContainer commandContainer) {
        this.commandContainer = commandContainer;
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
    public void onUpdateReceived(Update update)  {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();

            log.info("Received message: {}", message);

            try {
                if (message.startsWith(COMMAND_PREFIX)) {
                    String commandIdentifier = message.split(" ")[0].toLowerCase();
                    commandContainer.retrieveCommand(commandIdentifier).execute(update);
                } else {
                    commandContainer.retrieveCommand("NO").execute(update);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }
}
