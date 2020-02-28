package com.laibao.netty.reactordesignpattern.masterslave;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements  Runnable{

    private final ServerSocketChannel serverSocketChannel;

    private final Selector[] selectors = new Selector[RuntimeConstant.CORE_NUMBERS];

    private int selIndex = 0;

    private SubReactor[] subReactors = new SubReactor[RuntimeConstant.CORE_NUMBERS];

    private Thread[] threads = new Thread[RuntimeConstant.CORE_NUMBERS];


    public Acceptor(ServerSocketChannel serverSocketChannel){
        this.serverSocketChannel = serverSocketChannel;
        try{
            for(int i = 0;i < RuntimeConstant.CORE_NUMBERS;i++){
                selectors[i] = Selector.open();
                subReactors[i] = new SubReactor(selectors[i],serverSocketChannel,i);
                threads[i] = new Thread(subReactors[i]);
                threads[i].start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {
        try{
            /**
             * 接受client连接请求
             */
            SocketChannel socketChannel = serverSocketChannel.accept();

            if(socketChannel != null){
                System.out.println("client has connected,ip is" + socketChannel.getLocalAddress());
                socketChannel.configureBlocking(false);
                subReactors[selIndex].setRestart(true);
                /**
                 * 使一个阻塞住的selector操作立即返回
                 */
                selectors[selIndex].wakeup();
                SelectionKey selectionKey = socketChannel.register(selectors[selIndex],SelectionKey.OP_READ);
                selectors[selIndex].wakeup();//r[selIdx].
                subReactors[selIndex].setRestart(false);//重放线程
                //给定key一个附加的TcpHandler
                selectionKey.attach(new Handler(selectionKey,socketChannel));
                if(++selIndex == selectors.length){
                    selIndex = 0;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
