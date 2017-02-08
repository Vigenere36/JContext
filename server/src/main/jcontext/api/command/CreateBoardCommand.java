package jcontext.api.command;

public class CreateBoardCommand implements Command {
    private final String title;

    public CreateBoardCommand(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public CommandType getType() {
        return CommandType.CREATE_BOARD;
    }
}
