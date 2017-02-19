package jcontext.api.handler;

import jcontext.api.command.Command;
import jcontext.api.command.CommandType;
import jcontext.api.response.AckResponse;
import jcontext.api.response.Response;

public class TestHandler implements CommandHandler {
    @Override
    public Response handle(Command command) {
        if (command.commandType() != CommandType.TEST) return null;
        return new AckResponse();
    }
}
