package com.github.command;

import com.github.service.SendMessageInterface;
import com.github.service.SubscriptionServiceInterface;
import com.github.service.HelpUnknownService;
import com.github.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit test for CommandContainer")
public class CommandContainerTest {

    private CommandContainer commandContainer;

    @BeforeEach
    public void init() {
        // Мокаем все сервисы, чтобы создать реальные объекты команд
        SubscriptionServiceInterface subscriptionService = Mockito.mock(SubscriptionServiceInterface.class);
        HelpUnknownService helpUnknownService = Mockito.mock(HelpUnknownService.class);
        UserService userService = Mockito.mock(UserService.class);

        // Создаём все команды
        Command noCommand = new NoCommand(helpUnknownService);
        Command unknownCommand = new UnknownCommand(helpUnknownService);
        Command helpCommand = new HelpCommand(helpUnknownService);
        Command startCommand = new StartCommand(userService);
        Command trackCommand = new TrackCommand(subscriptionService);
        Command untrackCommand = new UntrackCommand(subscriptionService);
        Command listCommand = new ListCommand(subscriptionService);

        List<Command> commands = Arrays.asList(
                noCommand, unknownCommand, helpCommand, startCommand,
                trackCommand, untrackCommand, listCommand
        );
        commandContainer = new CommandContainer(commands);
    }

    @Test
    public void shouldReturnStartCommand() {
        Command command = commandContainer.retrieveCommand("/start");
        assertTrue(command instanceof StartCommand);
    }

    @Test
    public void shouldReturnHelpCommand() {
        Command command = commandContainer.retrieveCommand("/help");
        assertTrue(command instanceof HelpCommand);
    }

    @Test
    public void shouldReturnTrackCommand() {
        Command command = commandContainer.retrieveCommand("/track");
        assertTrue(command instanceof TrackCommand);
    }

    @Test
    public void shouldReturnUntrackCommand() {
        Command command = commandContainer.retrieveCommand("/untrack");
        assertTrue(command instanceof UntrackCommand);
    }

    @Test
    public void shouldReturnListCommand() {
        Command command = commandContainer.retrieveCommand("/list");
        assertTrue(command instanceof ListCommand);
    }

    @Test
    public void shouldReturnNoCommand() {
        Command command = commandContainer.retrieveCommand("NO");
        assertTrue(command instanceof NoCommand);
    }

    @Test
    public void shouldReturnUnknownCommand() {
        Command command = commandContainer.retrieveCommand("/jopa");
        assertTrue(command instanceof UnknownCommand);
    }
}
