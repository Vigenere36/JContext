package jcontext.api.handler;

import jcontext.api.command.Command;
import jcontext.api.response.Response;

public interface CommandHandler {
    Response handle(Command command);
}
