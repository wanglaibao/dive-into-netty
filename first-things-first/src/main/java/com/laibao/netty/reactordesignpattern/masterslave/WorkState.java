package com.laibao.netty.reactordesignpattern.masterslave;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class WorkState implements HandlerState{

    private SelectionKey selectionKey;

    private Handler handler;

    private String str;

    public WorkState(Handler handler,String str){
        this.handler = handler;
        this.str = str;
    }

    @Override
    public void handler(Handler handler, SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool) {
        // TODO: 2020/2/28
    }

    @Override
    public void changeState(Handler handler, SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool) {
        // TODO: 2020/2/28
    }
}
