package jcontext.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import jcontext.state.State;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;
import java.util.regex.Pattern;

import static jcontext.JContextModule.createMapBindingForHandlers;

public class DbHandlerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DbManager.class).asEagerSingleton();
        bind(Reflections.class).toInstance(new Reflections(new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(ClasspathHelper.forPackage(""))));
        createMapBindingForHandlers(new TypeLiteral<Class<? extends State>>(){}, new TypeLiteral<StateDbHandler>(){}, binder());
    }

    @Provides
    @Named("sqlResources")
    Set<String> getSqlResources(Reflections reflections) {
        return reflections.getResources(Pattern.compile(".*\\.sql"));
    }
}
