package jcontext.connection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import jcontext.api.command.Command;
import jcontext.api.command.CommandType;
import jcontext.api.handler.CommandHandler;
import jcontext.api.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class ApiDispatcher {
    private static final Logger log = LoggerFactory.getLogger(ApiDispatcher.class);
    private final Map<CommandType, CommandHandler> commandHandlers;
    private final ServerConnectionResponder responder;

    interface Factory {
        ApiDispatcher create(ServerConnectionResponder responder);
    }

    @Inject
    ApiDispatcher(Map<CommandType, CommandHandler> commandHandlers,
                  @Assisted ServerConnectionResponder responder) {
        this.commandHandlers = commandHandlers;
        this.responder = responder;
    }

    void handleCommand(Command command) {
        log.info("Handling command of type {}", command.commandType().name());
        Response response = commandHandlers.get(command.commandType()).handle(command);

        if (response != null) {
            responder.sendResponse(response);
            log.info("Sent response of type {}", response.responseType().name());
        }
    }
}
