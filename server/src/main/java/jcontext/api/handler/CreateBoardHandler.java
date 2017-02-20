package jcontext.api.handler;

import jcontext.api.command.CreateBoardCommand;
import jcontext.api.response.AckResponse;
import jcontext.api.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CreateBoardHandler implements CommandHandler<CreateBoardCommand> {
    @Override
    public Response handle(CreateBoardCommand command) {
        log.info("Handling a create board command with title {}", command.getTitle());
        return new AckResponse();
    }
}
