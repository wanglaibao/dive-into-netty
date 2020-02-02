package com.laibao.netty.inaction.chapter4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 未使用Netty的阻塞网络编程
 *
 */
public class PlainOioServer {

    public void serve(int port){
        try {
            //将服务器绑定到指定端口
            final ServerSocket serverSocket = new ServerSocket(port);

            for(;;) {
                //接受连接
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);

                //创建一个新的线程来处理该连接
                new Thread(() -> {
                    OutputStream out;
                    try {
                        //将消息写给已连接的客户端
                        out = clientSocket.getOutputStream();
                        out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                        out.flush();
                        //关闭连接
                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    //启动线程
                }).start();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
