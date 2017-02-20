package jcontext;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import jcontext.api.command.Command;
import jcontext.api.handler.CommandHandler;
import jcontext.database.StateDbHandler;
import jcontext.state.StateObject;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

@Slf4j
public class JContextModule extends AbstractModule {
    @Override
    protected void configure() {
        createMapBindingForHandlers(new TypeLiteral<Class<? extends Command>>(){}, new TypeLiteral<CommandHandler>(){});
        createMapBindingForHandlers(new TypeLiteral<Class<? extends StateObject>>(){}, new TypeLiteral<StateDbHandler>(){});
    }

    /**
     * Holy shit it actually works, don't try this at home
     * Used to automatically map a command->command handler, state->state db handler, etc
     */
    private <K, V> void createMapBindingForHandlers(TypeLiteral<K> typeClass, TypeLiteral<V> handlerTypeClass) {
        MapBinder<K, V> handlersForType = MapBinder.newMapBinder(binder(), typeClass, handlerTypeClass);

        Reflections reflections = new Reflections("jcontext");
        Set<Class<? extends V>> handlers = reflections.getSubTypesOf((Class) handlerTypeClass.getType());

        handlers.forEach(handler -> Lists.newArrayList(handler.getGenericInterfaces()).forEach(interfaceType -> {
            if (!(interfaceType instanceof ParameterizedType)) return;

            Type[] types = ((ParameterizedType) interfaceType).getActualTypeArguments();
            Lists.newArrayList(types).forEach(typeArgument -> {
                if (!(typeArgument instanceof Class)) return;
                handlersForType.addBinding((K) typeArgument).to(handler);
            });
        }));
    }
}
