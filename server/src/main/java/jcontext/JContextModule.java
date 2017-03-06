package jcontext;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Named;
import jcontext.api.handler.CommandHandlerModule;
import jcontext.connection.ServerConnectionModule;
import jcontext.database.DbHandlerModule;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

@Slf4j
public class JContextModule extends AbstractModule {
    private final JsonObject properties;

    JContextModule(JsonObject properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {
        install(new ServerConnectionModule());
        install(new CommandHandlerModule());
        install(new DbHandlerModule());
    }

    @Provides
    @Named("dbConnection")
    public String getDbConnection() {
        return properties.get("dbConnection").getAsString();
    }

    /**
     * I wouldn't recommend doing this in real production code
     * Used to automatically map a command->command handler, state->state db handler, etc
     */
    public static <K, V> void createMapBindingForHandlers(TypeLiteral<K> typeClass,
                                                          TypeLiteral<V> handlerTypeClass,
                                                          Binder binder) {
        MapBinder<K, V> handlersForType = MapBinder.newMapBinder(binder, typeClass, handlerTypeClass);

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
