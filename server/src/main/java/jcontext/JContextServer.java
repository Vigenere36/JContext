package jcontext;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Guice;
import jcontext.connection.ServerConnectionListener;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class JContextServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Guice.createInjector(new JContextModule(getProperties(args)))
                .getInstance(ServerConnectionListener.class).listen();
    }

    private static JsonObject getProperties(String[] args) throws IOException {
        JsonParser parser = new JsonParser();
        InputStream defaultProperties = JContextServer.class.getClassLoader().getResourceAsStream("properties.json");

        return (args.length < 1) ? parser.parse(new InputStreamReader(defaultProperties)).getAsJsonObject() :
                parser.parse(Files.toString(new File(args[0]), Charsets.UTF_8)).getAsJsonObject();
    }
}