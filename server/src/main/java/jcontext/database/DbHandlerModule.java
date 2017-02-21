package jcontext.database;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import jcontext.state.State;

import static jcontext.JContextModule.createMapBindingForHandlers;

public class DbHandlerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DbManager.class).asEagerSingleton();
        createMapBindingForHandlers(new TypeLiteral<Class<? extends State>>(){}, new TypeLiteral<StateDbHandler>(){}, binder());
    }
}
