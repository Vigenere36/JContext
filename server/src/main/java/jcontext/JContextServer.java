package jcontext;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Module;
import jcontext.connection.ServerConnectionListener;
import jcontext.connection.ServerConnectionModule;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JContextServer {
    public static void main(String[] args) throws InterruptedException {
        Guice.createInjector(getModules()).getInstance(ServerConnectionListener.class).listen();
    }

    private static List<Module> getModules() {
        return ImmutableList.of(new JContextModule(), new ServerConnectionModule());
    }
}