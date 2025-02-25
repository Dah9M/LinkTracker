package com.github.command;

import com.github.service.SendMessageInterface;

import java.util.HashMap;
import java.util.Map;

import static com.github.command.CommandName.*;

public class CommandContainer {
    private final Map<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(SendMessageInterface sendMessageService) {
        commandMap = new HashMap<>();
        commandMap.put(START.getCommandName(), new StartCommand(sendMessageService));
        commandMap.put(HELP.getCommandName(), new HelpCommand(sendMessageService));
        commandMap.put(LIST.getCommandName(), new ListCommand(sendMessageService));
        commandMap.put(TRACK.getCommandName(), new TrackCommand(sendMessageService));
        commandMap.put(UNTRACK.getCommandName(), new UntrackCommand(sendMessageService));
        commandMap.put(NO.getCommandName(), new NoCommand(sendMessageService));

        unknownCommand = new UnknownCommand(sendMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
