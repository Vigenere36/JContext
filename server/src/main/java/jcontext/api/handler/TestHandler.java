package jcontext.api.handler;

import jcontext.api.command.TestCommand;
import jcontext.api.response.AckResponse;
import jcontext.api.response.Response;

class TestHandler implements CommandHandler<TestCommand> {
    @Override
    public Response handle(TestCommand command) {
        return new AckResponse();
    }
}
