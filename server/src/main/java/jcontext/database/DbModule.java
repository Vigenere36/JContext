package jcontext.database;

import com.google.inject.AbstractModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbModule extends AbstractModule {
    @Override
    protected void configure() {
        /*
        MapBinder<Class<? extends StateObject>, StateDbHandler> dbHandlersForState =
                MapBinder.newMapBinder(binder(),
                        new TypeLiteral<Class<? extends StateObject>>() {},
                        new TypeLiteral<StateDbHandler>() {});

        Reflections reflections = new Reflections("jcontext");
        Set<Class<? extends StateDbHandler>> dbHandlers = reflections.getSubTypesOf(StateDbHandler.class);

        dbHandlers.forEach(dbHandler -> {
            Lists.newArrayList(dbHandler.getGenericInterfaces()).forEach(interfaceType -> {
                if (!(interfaceType instanceof ParameterizedType)) return;

                Type[] types = ((ParameterizedType) interfaceType).getActualTypeArguments();
                Lists.newArrayList(types).forEach(typeArgument -> {
                    if (!(typeArgument instanceof Class)) return;
                    dbHandlersForState.addBinding((Class<? extends StateObject>) typeArgument).to(dbHandler);
                });
            });
        });*/
    }
}
