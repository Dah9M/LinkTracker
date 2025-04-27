package command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CommandContainer {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandContainer(List<Command> commandList) {
        for (Command command : commandList) {
            commandMap.put(command.getCommandName(), command);
        }
        log.info("Инициализирован контейнер команд. Зарегистрировано {} команд.", commandList.size());
    }

    public Command retrieveCommand(String commandIdentifier) {
        Command command = commandMap.getOrDefault(commandIdentifier, commandMap.get("UNKNOWN"));

        if (command.getCommandName().equals("UNKNOWN")) {
            log.warn("Команда '{}' не найдена. используется команда 'UNKNOWN'.", commandIdentifier);
        } else {
            log.info("Извлечена команда '{}'.", commandIdentifier);
        }

        return command;
    }
}
