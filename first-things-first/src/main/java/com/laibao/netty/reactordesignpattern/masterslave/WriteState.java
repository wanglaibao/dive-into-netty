package com.laibao.netty.reactordesignpattern.masterslave;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class WriteState implements HandlerState{

    private Handler handler;

    private String str;

    public WriteState(Handler handler,String str){
        this.handler = handler;
        this.str = str;
    }


    @Override
    public void handler(Handler handler, SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool) {
        String str = "Your message has send to is:" +this.str ;
        try{
            ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
            while(byteBuffer.hasRemaining()){
                socketChannel.write(byteBuffer);//回传给client的回应字符串，发送buf的position位置，到limit位置为止之间的内容
            }
            selectionKey.interestOps(SelectionKey.OP_READ);//通过key改变通道注册的事件
            selectionKey.selector().wakeup();//使一个阻塞住的selector操作立即返回
            handler.setHandlerState(new ReadState());
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void changeState(Handler handler, SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool) {

    }
}
