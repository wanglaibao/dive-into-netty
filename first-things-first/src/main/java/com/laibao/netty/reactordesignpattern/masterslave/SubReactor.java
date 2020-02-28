package com.laibao.netty.reactordesignpattern.masterslave;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SubReactor implements  Runnable{

    private final ServerSocketChannel serverSocketChannel;

    private final Selector selector;

    private boolean restart = false;

    private int num;

    public SubReactor(Selector selector,ServerSocketChannel serverSocketChannel,int num){
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
        this.num = num;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){//在线程被中断前持续运行
            //  System.out.println("waiting for restart");
            while(!Thread.interrupted() && !restart){//在线程被中断前以及被指定重启前持续运行
                try{
                    if(selector.select()==0){
                        continue;//若没有事件就绪则不往下执行
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                //取得所有已就绪事件的key集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while(it.hasNext()){
                    dispatch((SelectionKey)(it.next()));//根据事件的key进行调度
                    it.remove();
                }

            }
        }
    }


    public void dispatch(SelectionKey selectionKey){
        Runnable runnable = (Runnable)selectionKey.attachment();
        if(runnable != null){
            runnable.run();
        }
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }
}
