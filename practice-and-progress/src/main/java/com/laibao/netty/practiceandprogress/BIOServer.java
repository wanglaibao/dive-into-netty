package com.laibao.netty.practiceandprogress;

import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {

    private ServerSocket serverSocket;

    public BIOServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("服务端启动成功, 端口: "+port);
        } catch (Exception ex) {
            System.out.println("服务端启动失败, 端口: "+port);
            ex.printStackTrace();
        }
    }

    public void start() {
        new Thread(() -> doStart()).start();
    }


    private void doStart() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new BIOServerHandler(socket).handler();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
