package jcontext.api.handler;

import com.google.inject.Inject;
import jcontext.api.command.CreateBoardCommand;
import jcontext.api.response.AckResponse;
import jcontext.api.response.Response;
import jcontext.database.DbManager;
import jcontext.state.Board;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CreateBoardHandler implements CommandHandler<CreateBoardCommand> {
    @Inject private DbManager dbManager;

    @Override
    public Response handle(CreateBoardCommand command) {
        log.info("Handling a create board command with title {}", command.getTitle());
        dbManager.insert(new Board(command.getTitle()));

        return new AckResponse();
    }
}
