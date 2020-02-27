package com.laibao.netty.reactordesignpattern.masterslave;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public interface HandlerState {

    /**
     *
     * @param handler
     * @param selectionKey
     * @param socketChannel
     * @param pool
     */
    void handler(Handler handler , SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool);

    /**
     *
     * @param handler
     * @param selectionKey
     * @param socketChannel
     * @param pool
     */
    void changeState(Handler handler,SelectionKey selectionKey,SocketChannel socketChannel,ThreadPoolExecutor pool);
}
