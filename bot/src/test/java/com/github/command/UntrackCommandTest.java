package com.github.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.command.CommandName.UNTRACK;
import static com.github.command.UntrackCommand.UNTRACK_MESSAGE;


@DisplayName("Unit test for UntrackCommand")
public class UntrackCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return UNTRACK.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return UNTRACK_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UntrackCommand(sendMessageService);
    }
}
