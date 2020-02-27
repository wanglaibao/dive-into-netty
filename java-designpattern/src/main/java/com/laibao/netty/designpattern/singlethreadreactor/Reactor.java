package com.laibao.netty.designpattern.singlethreadreactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


class Reactor implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public Reactor(){
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress(8989);
            serverSocketChannel.socket().bind(address);

            selector = Selector.open();
            /**
             * 注册接收 OP_ACCEPT 事件
             */
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            /**
             *  attach callback object: Acceptor
             */
            selectionKey.attach(new Acceptor());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                int selectedNumber = selector.select();
                if (selectedNumber <= 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    dispatch(selectionKey);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void dispatch(SelectionKey selectionKey) {
        Runnable handler = (Runnable) selectionKey.attachment();
        /**
         * 调用之前attach绑定到选择键的handler处理器对象
         */
        if (handler != null) {
            handler.run();
        }
    }

    /**
     * 接收新连接
     */
    class Acceptor implements Runnable {
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    new Handler(selector, socketChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        try{
            new Thread(new Reactor()).start();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
