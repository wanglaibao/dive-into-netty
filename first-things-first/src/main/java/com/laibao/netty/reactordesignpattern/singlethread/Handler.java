package com.laibao.netty.reactordesignpattern.singlethread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable{

    private final SocketChannel socketChannel;

    private final SelectionKey selectionKey;

    private int state;

    public Handler(SocketChannel socketChannel, SelectionKey selectionKey) {
        this.socketChannel = socketChannel;
        this.selectionKey = selectionKey;
        this.state = 0;
    }

    @Override
    public void run() {

        try{

            if (state == 0) {
                read();
            }else {
                send();
            }
        }catch (Exception ex){
            System.out.println("一个客户端关闭了");
            closeChannel();
        }
    }


    private synchronized void read() throws IOException {

        byte[] byteArr = new byte[1024];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArr);

        int readNumber = socketChannel.read(byteBuffer);

        if (readNumber == -1) {
            closeChannel();
            return;
        }

        String str = new String(byteArr);

        if (str != null && !str.equals("")) {
            System.out.println(socketChannel.socket().getRemoteSocketAddress().toString()+">>>>"+str);
            process(str);
            state = 1;
            selectionKey.interestOps(SelectionKey.OP_WRITE);
            selectionKey.selector().wakeup();
        }
    }


    private synchronized void send() throws IOException {

        String message = "message to be sent to "+socketChannel.socket().getRemoteSocketAddress()+"\r\n";

        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }

        state = 0;
        selectionKey.interestOps(SelectionKey.OP_READ);
        selectionKey.selector().wakeup();

    }


    private void process(String str) {
        System.out.println(str);
    }

    private void closeChannel() {
        try {
            selectionKey.cancel();
            socketChannel.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
