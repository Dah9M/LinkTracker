package com.github.command;

public enum CommandName {
    START("/start"),
    HELP("/help"),
    TRACK("/track"),
    UNTRACK("/untrack"),
    LIST("/list"),
    NO("NO");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
