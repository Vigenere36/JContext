package jcontext.api.handler;

import jcontext.api.command.Command;
import jcontext.api.command.CreateBoardCommand;
import jcontext.api.response.AckResponse;
import jcontext.api.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateBoardHandler implements CommandHandler {
    private static final Logger log = LoggerFactory.getLogger(CreateBoardHandler.class);

    @Override
    public Response handle(Command command) {
        if (!(command instanceof CreateBoardCommand)) return null;
        CreateBoardCommand createBoardCommand = (CreateBoardCommand) command;

        log.info("Handling a create board command with title {}", createBoardCommand.getTitle());
        return new AckResponse();
    }
}
