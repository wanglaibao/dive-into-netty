package com.laibao.netty.inaction.ch1;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) {
        try {
            /**
             *  bossSelector
             */
            Selector bossSelector = Selector.open();

            /**
             * workerSelector
             */
            Selector workerSelector = Selector.open();

            /**
             * 类似于Netty中的 EventLoopGroup
             */
            new Thread(() -> {
                try {
                    /**
                     * 对应BIO编程中服务端启动
                     */
                    ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                    listenerChannel.socket().bind(new InetSocketAddress(8000));
                    listenerChannel.configureBlocking(false);
                    listenerChannel.register(bossSelector, SelectionKey.OP_ACCEPT);

                    while (true) {
                        /**
                         * (1)监测是否有新的连接,这里的1指的是阻塞的时间为1ms
                         */
                        if (bossSelector.select(1) > 0) {
                            Set<SelectionKey> set = bossSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();

                            while (keyIterator.hasNext()) {
                                SelectionKey key = keyIterator.next();

                                if (key.isAcceptable()) {
                                    try {
                                        /**
                                         * (2)每来一个新连接,不需要创建一个线程,而是直接注册到workerSelector
                                         */
                                        SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                        clientChannel.configureBlocking(false);
                                        clientChannel.register(workerSelector, SelectionKey.OP_READ);
                                    } finally {
                                        keyIterator.remove();
                                    }
                                }

                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }).start();



            /**
             * 类似于Netty中的 EventLoopGroup
             */
            new Thread(() -> {
                try {
                    while (true) {
                        /**
                         * (3)批量轮询是否有哪些连接有数据可读,这里的1指的是阻塞的时间为 1ms
                         */
                        if (workerSelector.select(1) > 0) {
                            Set<SelectionKey> set = workerSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();

                            while (keyIterator.hasNext()) {
                                SelectionKey key = keyIterator.next();

                                if (key.isReadable()) {
                                    try {
                                        SocketChannel clientChannel = (SocketChannel) key.channel();
                                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                        /**
                                         * (4)面向Buffer
                                         */
                                        clientChannel.read(byteBuffer);
                                        byteBuffer.flip();
                                        System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer).toString());
                                    } finally {
                                        keyIterator.remove();
                                        key.interestOps(SelectionKey.OP_READ);
                                    }
                                }

                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }
}
