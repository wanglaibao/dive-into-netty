package com.laibao.netty.reactordesignpattern.singlethread;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SingleReactor implements Runnable{

    private ServerSocketChannel serverSocketChannel;

    private  Selector selector;


    public SingleReactor(int serverPort) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress socketAddress = new InetSocketAddress(serverPort);
            serverSocketChannel.socket().bind(socketAddress);

            selector = Selector.open();
            SelectionKey selectionKey= serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Acceptor(serverSocketChannel,selector));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println("waiting for new event on port..."+serverSocketChannel.socket().getLocalPort());
                int eventNumber = selector.select();
                if (eventNumber <= 0) {
                    //如果没有事件就绪则不往下面继续执行
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    dispatch((SelectionKey)(iterator.next()));
                    iterator.remove();
                }
                //selectionKeys.clear();
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
}
