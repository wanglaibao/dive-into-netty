package com.laibao.netty.designpattern.mainsubreactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


class MainReactor {

    private ServerSocketChannel serverSocketChannel;

    private AtomicInteger next = new AtomicInteger(0);

    /**
     * selectors集合,引入多个selector选择器
     */
    private Selector[] selectors = new Selector[RuntimeContent.CORES_NUMBER];

    /**
     * 引入多个子反应器
     */
    private SubReactor[] subReactors = new SubReactor[RuntimeContent.CORES_NUMBER];


    MainReactor() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(9898);
        serverSocketChannel.socket().bind(address);

        /**
         *
         * 初始化多个selector选择器,并且每一个selector绑定到一个SubReactor上面
         *
         */
        for (int i = 0;i < selectors.length;i++){
            selectors[i] = Selector.open();
            subReactors[i] = new SubReactor(selectors[i]);
        }

        /**
         * 第一个selector,负责监控新连接事件
         */
        SelectionKey selectionKey = serverSocketChannel.register(selectors[0], SelectionKey.OP_ACCEPT);

        /**
         * 附加新连接处理handler处理器到SelectionKey（选择键）
         */
        selectionKey.attach(new AcceptorHandler());


        //第一个子反应器，一子反应器负责一个选择器
        //SubReactor subReactor1 = new SubReactor(selectors[0]);
        //第二个子反应器，一子反应器负责一个选择器
        //SubReactor subReactor2 = new SubReactor(selectors[1]);
        //subReactors = new SubReactor[]{subReactor1, subReactor2};
    }

    private void startService() {
        for (SubReactor subReactor: subReactors) {
            new Thread(subReactor).start();
        }
    }

    //反应器
    class SubReactor implements Runnable {
        //每条线程负责一个选择器的查询
        final Selector selector;

        public SubReactor(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        //Reactor负责dispatch收到的事件
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        dispatch(selectionKey);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        private void dispatch(SelectionKey selectionKey) {
            Runnable handler = (Runnable) selectionKey.attachment();
            //调用之前attach绑定到选择键的handler处理器对象
            if (handler != null) {
                handler.run();
            }
        }
    }


    // Handler:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null){
                    new MultiThreadEchoHandler(selectors[next.get()], socketChannel);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (next.incrementAndGet() == selectors.length) {
                next.set(0);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        MainReactor server = new MainReactor();
        server.startService();
    }

}
