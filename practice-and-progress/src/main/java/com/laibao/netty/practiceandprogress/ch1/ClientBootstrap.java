package com.laibao.netty.practiceandprogress.ch1;

public class ClientBootstrap {

    private static final int PORT = 8000;

    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        BIOClient client = new BIOClient(PORT,HOST);
        client.start();
    }
}
