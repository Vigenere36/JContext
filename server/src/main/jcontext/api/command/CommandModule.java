package jcontext.api.command;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import jcontext.api.handler.CommandHandler;

import java.util.Arrays;

public class CommandModule extends AbstractModule {
    @Override
    protected void configure() {
        MapBinder<CommandType, CommandHandler> handlers = MapBinder.newMapBinder(
                binder(), CommandType.class, CommandHandler.class);
        Arrays.asList(CommandType.values()).forEach(type -> handlers.addBinding(type).to(type.getHandler()));
    }
}
