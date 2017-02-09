package jcontext.api.command;

import jcontext.api.handler.CommandHandler;
import jcontext.api.handler.CreateBoardHandler;

public enum CommandType {
    CREATE_BOARD(CreateBoardHandler.class);

    private final Class<? extends CommandHandler> handlerClass;

    <T extends CommandHandler> CommandType(Class<T> handlerClass) {
        this.handlerClass = handlerClass;
    }

    Class<? extends CommandHandler> getHandler() {
        return handlerClass;
    }
}
