package jcontext.api.handler;

import jcontext.api.command.Command;
import jcontext.api.command.CreateBoardCommand;

public class CreateBoardHandler implements CommandHandler {
    @Override
    public void handle(Command command) {
        if (!(command instanceof CreateBoardCommand)) return;

        CreateBoardCommand createBoardCommand = (CreateBoardCommand) command;
        System.out.printf("Handling a create board command with title %s", createBoardCommand.getTitle());
    }
}
