package jcontext;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import jcontext.api.command.CommandModule;
import jcontext.connection.ServerConnectionModule;
import jcontext.connection.ServerConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JContextServer {
    private static final Logger log = LoggerFactory.getLogger(JContextServer.class);

    public static void main(String[] args) {
        // Initialize Guice
        Injector injector = Guice.createInjector(getModules());

        // Initialize database

        // Initialize server listener
        try {
            injector.getInstance(ServerConnectionListener.class).listen();
        } catch (InterruptedException e) {
            log.error("Server connection interrupted", e);
        }
    }

    private static List<Module> getModules() {
        return ImmutableList.of(new CommandModule(), new ServerConnectionModule());
    }
}