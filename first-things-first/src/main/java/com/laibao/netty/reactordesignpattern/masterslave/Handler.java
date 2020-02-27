package com.laibao.netty.reactordesignpattern.masterslave;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Handler implements  Runnable{

    /**
     * 线程池
     */
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(RuntimeConstant.CORE_NUMBERS,RuntimeConstant.CORE_NUMBERS,30, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    /**
     * 以状态模式实现handler
     */
    HandlerState handlerState;

    private SelectionKey selectionKey;

    private SocketChannel socketChannel;

    public Handler(SelectionKey selectionKey, SocketChannel socketChannel){
        this.selectionKey = selectionKey;
        this.socketChannel = socketChannel;
        /**
         * 初始状态设定为READING
         */
        handlerState = new ReadState();
    }


    @Override
    public void run() {
        try{
            handlerState.handler(this,selectionKey,socketChannel,pool);
        }catch(Exception ex){
            System.out.println("warning A client has been closed");
            closeChannel();
        }
    }

    public void closeChannel(){
        try{
            selectionKey.cancel();
            socketChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setHandlerState(HandlerState handlerState){
        this.handlerState = handlerState;
    }

    public static ThreadPoolExecutor getPool() {
        return pool;
    }

    public HandlerState getHandlerState() {
        return handlerState;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
}
