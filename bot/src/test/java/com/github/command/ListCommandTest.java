package com.github.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.command.CommandName.LIST;
import static com.github.command.ListCommand.LIST_MESSAGE;

@DisplayName("Unit test for ListCommand")
public class ListCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return LIST.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return LIST_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new ListCommand(sendMessageService);
    }
}
