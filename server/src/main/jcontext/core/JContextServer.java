package jcontext.core;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import jcontext.api.ApiDispatcher;
import jcontext.api.command.CommandModule;
import jcontext.api.command.CreateBoardCommand;

import java.util.List;

public class JContextServer {
    public static void main(String[] args) {
        // Initialize Guice
        Injector injector = Guice.createInjector(getModules());

        ApiDispatcher dispatcher = injector.getInstance(ApiDispatcher.class);
        dispatcher.handleCommand(new CreateBoardCommand("a new board!"));
        // Initialize database
        // Initialize client connection listener
        // Initialize api listeners for connection
    }

    private static List<Module> getModules() {
        return ImmutableList.of(new CommandModule());
    }
}