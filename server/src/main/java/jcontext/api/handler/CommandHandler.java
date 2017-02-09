package jcontext.api.handler;

import jcontext.api.command.Command;

public interface CommandHandler {
    void handle(Command command);
}
