package jcontext.connection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import jcontext.api.command.Command;
import jcontext.api.handler.CommandHandler;
import jcontext.api.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
class ApiDispatcher {
    private final Map<Class<? extends Command>, CommandHandler> commandHandlers;
    private final ConnectionResponder responder;

    interface Factory {
        ApiDispatcher create(ConnectionResponder responder);
    }

    @Inject
    ApiDispatcher(Map<Class<? extends Command>, CommandHandler> commandHandlers,
                  @Assisted ConnectionResponder responder) {
        this.commandHandlers = commandHandlers;
        this.responder = responder;
    }

    void handleCommand(Command command) {
        log.debug("Handling command of type {}", command.getClass());
        Response response = commandHandlers.get(command.getClass()).handle(command);

        if (response != null) {
            responder.sendResponse(response);
            log.debug("Sent response of type {}", response.responseType().name());
        }
    }
}