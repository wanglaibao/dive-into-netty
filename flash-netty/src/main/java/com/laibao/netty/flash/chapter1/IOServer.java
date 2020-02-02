package com.laibao.netty.flash.chapter1;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server端首先创建了一个serverSocket来监听8000端口,然后创建一个线程,线程里面不断调用阻塞方法serversocket.accept()来获取新的连接见(2),
 * 当获取到新的连接之后,给每条连接创建一个新的线程,这个线程负责从该连接中读取数据见(3)，
 * 然后读取数据是以字节流的方式见(4)。
 */
public class IOServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);

            /**
             * (1)接收新连接线程
             */
            new Thread(() -> {
                while (true){
                    try {
                        /**
                         * (2)阻塞方法获取新的连接
                         */
                        Socket socket = serverSocket.accept();

                        /**
                         *  (3)每一个新的连接都创建一个线程,负责读取数据
                         */
                        new Thread(() -> {
                            try {
                                InputStream inputStream = socket.getInputStream();
                                int len = -1;
                                byte[] data = new byte[1024];
                                /**
                                 * (4) 按字节流方式读取数据
                                 */
                                while ((len = inputStream.read(data)) != -1) {
                                    System.out.println(new String(data, 0, len));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
