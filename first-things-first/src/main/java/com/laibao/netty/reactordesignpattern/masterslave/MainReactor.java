package com.laibao.netty.reactordesignpattern.masterslave;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MainReactor implements Runnable{

    private  ServerSocketChannel serverSocketChannel;

    private  Selector selector;

    public MainReactor(int port){

        try {
            this.serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress socketAddress = new InetSocketAddress(port);
            serverSocketChannel.socket().bind(socketAddress);

            selector = Selector.open();
            SelectionKey selectionKey = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Acceptor(serverSocketChannel));//指定附加对象

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            System.out.println("waiting for new event on port:"+serverSocketChannel.socket().getLocalPort());
            try{
                if(selector.select() == 0){
                    /**
                     * 没有连接进来，则持续等待
                     */
                    continue;
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while(iterator.hasNext()){
                dispatch(iterator.next());
                /**
                 * 调用完后移除
                 */
                iterator.remove();
            }
        }

    }

    /**
     *
     * @param selectionKey
     */
    private void dispatch(SelectionKey selectionKey){
        Runnable runnable = (Runnable)selectionKey.attachment();
        if(runnable != null){
            runnable.run();
        }
    }
}
