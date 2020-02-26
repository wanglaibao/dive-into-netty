package com.laibao.netty.reactordesignpattern.multithread;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reactor implements Runnable{

    private final Selector selector;

    private final ServerSocketChannel serverSocketChannel;

    private static final int WORKER_POOL_SIZE = 10;

    protected static ExecutorService workerPool;

    static {
        workerPool = Executors.newFixedThreadPool(WORKER_POOL_SIZE);
    }

    public Reactor(int port) throws Exception {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        /**
         * Register the server socket channel with interest-set set to ACCEPT operation
         */
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }


    /**
     * 应用主程序入口
     * @param args
     */
    public static void main(String[] args) {
        try {
            new Thread(new Reactor(9090)).start();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            while (true) {
                selector.select();
                Iterator iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = (SelectionKey) iterator.next();
                    iterator.remove();
                    dispatch(selectionKey);
                    /*
                        Runnable r = (Runnable) sk.attachment();
                        if (r != null){
                            r.run();
                        }
                    */

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
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
    }*/


    private void dispatch(SelectionKey selectionKey) {
        Runnable runnable = (Runnable)(selectionKey.attachment());
        if (runnable != null) {
            runnable.run();
        }
    }


    private class Acceptor implements Runnable {
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    new Handler(selector, socketChannel);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
