package com.github.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.command.CommandName.NO;
import static com.github.command.NoCommand.NO_MESSAGE;

@DisplayName("Unit test for NoCommand")
public class NoCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return NO.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return NO_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new NoCommand(sendMessageService);
    }
}
