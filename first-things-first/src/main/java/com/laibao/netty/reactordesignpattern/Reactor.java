package com.laibao.netty.reactordesignpattern;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable{

    private final Selector selector;

    private final ServerSocketChannel serverSocket;

    public Reactor(int port) throws Exception {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey selectionKey = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator iterator = selected.iterator();
                while (iterator.hasNext()){
                    dispatch((SelectionKey)(iterator.next()));
                }
                selected.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void dispatch(SelectionKey selectionKey) {
        Runnable runnable = (Runnable)(selectionKey.attachment());
        if (runnable != null) {
            runnable.run();
        }
    }


    private class Acceptor implements Runnable {
        // inner class
        public void run() {
            try {
                SocketChannel socketChannel = serverSocket.accept();
                if (socketChannel != null) {
                    //new Handler(selector, socketChannel);
                }
            }
            catch(Exception ex) {
                /* ... */
                ex.printStackTrace();
            }
        }
    }
}
