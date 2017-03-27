package jcontext;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import io.netty.channel.ChannelFuture;
import jcontext.connection.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class JContextServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Injector injector = Guice.createInjector(new JContextModule(getProperties(args)));

        List<Connection> connections = injector.getInstance(new Key<List<Connection>>() {});
        List<ChannelFuture> futures = Lists.newArrayList();

        for (Connection connection : connections) {
            futures.add(connection.listen());
        }

        for (ChannelFuture future : futures) {
            future.sync();
        }
    }

    private static JsonObject getProperties(String[] args) throws IOException {
        JsonParser parser = new JsonParser();
        InputStream defaultProperties = JContextServer.class.getClassLoader().getResourceAsStream("properties.json");

        return (args.length < 1) ? parser.parse(new InputStreamReader(defaultProperties)).getAsJsonObject() :
                parser.parse(Files.toString(new File(args[0]), Charsets.UTF_8)).getAsJsonObject();
    }
}