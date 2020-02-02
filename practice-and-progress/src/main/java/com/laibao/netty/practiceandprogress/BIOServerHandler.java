package com.laibao.netty.practiceandprogress;

import java.io.InputStream;
import java.net.Socket;

public class BIOServerHandler {

    private static final int MAX_DATA_LEN = 1024;

    private final Socket socket;

    public BIOServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void handler() {
        System.out.println("新客户端接入");
        new Thread(() -> doHandler()).start();
    }


    private void doHandler(){
        while (true) {
            try {
                InputStream inputStream = socket.getInputStream();
                int len;
                byte[] data = new byte[MAX_DATA_LEN];
                while ((len = inputStream.read(data)) != -1) {
                    String message = new String(data, 0, len);
                    System.out.println("客户端传来消息:" + message);
                    socket.getOutputStream().write(data);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
