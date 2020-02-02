package com.laibao.netty.practiceandprogress.ch1;

import java.net.Socket;

public class BIOClient {

    private final int port;

    private final String host;

    private Socket socket;

    public BIOClient(int port,String host) {
        this.port = port;
        this.host = host;
        try {
            this.socket = new Socket(host,port);
            System.out.println("客户端启动成功");
        } catch (Exception ex) {
            System.out.println("客户端启动失败");
            ex.printStackTrace();

        }
    }

    public void start() {
        new Thread(() -> doStart()).start();
    }

    private void doStart() {
        new BIOClientHandler(socket).handler();
    }
}
