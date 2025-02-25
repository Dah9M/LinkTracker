package com.github.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.command.CommandName.TRACK;
import static com.github.command.TrackCommand.TRACK_MESSAGE;

@DisplayName("Unit test for TrackCommand")
public class TrackCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return TRACK.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return TRACK_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new TrackCommand(sendMessageService);
    }
}
