package com.github.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.command.UnknownCommand.UNKNOWN_MESSAGE;

@DisplayName("Unit test for UnknownCommand")
public class UnknownCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return "/jopa";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendMessageService);
    }
}
