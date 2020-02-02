package com.laibao.netty.practiceandprogress.ch1;

import java.net.Socket;

public class BIOClientHandler {

    private final Socket socket;

    public BIOClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void handler() {
        doHandler();
    }

    private void doHandler(){
        while (true) {
            String message = "hello,jinGe";
            System.out.println("客户端发送数据:"+message);
            try {
                socket.getOutputStream().write(message.getBytes());
                Thread.sleep(5000);
            } catch (Exception ex) {
                System.out.println("客户端发送数据失败");
                ex.printStackTrace();
            }
        }
    }



}
