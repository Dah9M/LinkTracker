package com.github.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.command.CommandName.HELP;
import static com.github.command.HelpCommand.HELP_MESSAGE;

@DisplayName("Unit test for HelpCommand")
public class HelpCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return HELP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new HelpCommand(sendMessageService);
    }
}
