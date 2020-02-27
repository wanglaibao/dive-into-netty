package com.laibao.netty.reactordesignpattern.masterslave;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class ReadState implements HandlerState{

    private SelectionKey selectionKey;

    @Override
    public void handler(Handler handler, SelectionKey selectionKey, SocketChannel socketChannel, ThreadPoolExecutor pool){
        this.selectionKey = selectionKey;
        byte[] byteArr = new byte[1024];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArr);
        try{
            //读取字符串
            int numBytes = socketChannel.read(byteBuffer);
            if(numBytes == -1){
                System.out.println("[Warning!] A client has been closed");
                handler.closeChannel();
                return ;
            }

            String str = new String(byteArr);//将读取到的byte内容转为字符串
            System.out.println(h.getSc().getLocalAddress().toString()+"说："+str);
            if((str!=null) && !str.equals("")){
                h.setState(new WorkState(h,str));//改变状态
                pool.execute(new WorkerThread(h,str));//do process in worker thread
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
