package jcontext.api;

import com.google.inject.Inject;
import jcontext.api.command.Command;
import jcontext.api.command.CommandType;
import jcontext.api.handler.CommandHandler;

import java.util.Map;

public class ApiDispatcher {
    private final Map<CommandType, CommandHandler> commandHandlers;

    @Inject
    ApiDispatcher(Map<CommandType, CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    public void handleCommand(Command command) {
        commandHandlers.get(command.getType()).handle(command);
    }
}
