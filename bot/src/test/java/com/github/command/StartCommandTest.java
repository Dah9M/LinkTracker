package com.github.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.command.CommandName.START;
import static com.github.command.StartCommand.START_MESSAGE;

@DisplayName("Unit test for StartCommand")
public class StartCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return START.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return START_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StartCommand(sendMessageService);
    }
}
