package jcontext.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JContextServerTest {
    private JContextServer server;

    @Before
    public void initialize() {
        server = new JContextServer();
    }

    @Test
    public void test() {
        assertTrue(server.test());
    }
}