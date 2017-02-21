package jcontext.api.handler;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import jcontext.api.command.Command;

import static jcontext.JContextModule.createMapBindingForHandlers;

public class CommandHandlerModule extends AbstractModule {
    @Override
    protected void configure() {
        createMapBindingForHandlers(new TypeLiteral<Class<? extends Command>>(){}, new TypeLiteral<CommandHandler>(){}, binder());
    }
}
