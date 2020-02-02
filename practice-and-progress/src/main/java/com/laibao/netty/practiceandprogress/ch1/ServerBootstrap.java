package com.laibao.netty.practiceandprogress.ch1;

public class ServerBootstrap {
    private static final int PORT = 8000;

    public static void main(String[] args) {
        BIOServer server = new BIOServer(PORT);
        server.start();
    }
}
