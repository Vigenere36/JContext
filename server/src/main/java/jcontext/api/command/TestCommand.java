package jcontext.api.command;

public class TestCommand implements Command {
    @Override
    public CommandType commandType() {
        return CommandType.TEST;
    }
}
