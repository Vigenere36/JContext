package jcontext.api.handler;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import jcontext.api.command.Command;
import jcontext.api.response.Response;
import jcontext.connection.ServerConnectionResponder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ApiDispatcher {
    private final Map<Class<? extends Command>, CommandHandler> commandHandlers;
    private final ServerConnectionResponder responder;

    public interface Factory {
        ApiDispatcher create(ServerConnectionResponder responder);
    }

    @Inject
    public ApiDispatcher(Map<Class<? extends Command>, CommandHandler> commandHandlers,
                  @Assisted ServerConnectionResponder responder) {
        this.commandHandlers = commandHandlers;
        this.responder = responder;
    }

    public void handleCommand(Command command) {
        log.debug("Handling command of type {}", command.getClass());
        Response response = commandHandlers.get(command.getClass()).handle(command);

        if (response != null) {
            responder.sendResponse(response);
            log.debug("Sent response of type {}", response.responseType().name());
        }
    }
}
