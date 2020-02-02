package com.laibao.netty.inaction.ch1;

import java.net.Socket;
import java.time.LocalDate;

public class BIOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while (true) {
                    try {
                        socket.getOutputStream().write((LocalDate.now() + ": hello world").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
           }
        }).start();
    }
}
