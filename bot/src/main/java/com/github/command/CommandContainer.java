package com.github.command;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandContainer {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandContainer(List<Command> commandList) {
        for (Command command : commandList) {
            commandMap.put(command.getCommandName(), command);
        }

    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, commandMap.get("UNKNOWN"));
    }
}
