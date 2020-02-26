package com.laibao.netty.reactordesignpattern.singlethread;


import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable{

    private final ServerSocketChannel serverSocketChannel;

    private final Selector selector;


    public Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }


    @Override
    public void run() {

        try{

            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println(socketChannel.socket().getRemoteSocketAddress() + "is connected");
            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                selector.wakeup();
                selectionKey.attach(new Handler(socketChannel,selectionKey));
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
