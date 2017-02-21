package jcontext.api.handler;

import jcontext.api.command.Command;
import jcontext.api.response.Response;

interface CommandHandler<T extends Command> {
    Response handle(T command);
}
