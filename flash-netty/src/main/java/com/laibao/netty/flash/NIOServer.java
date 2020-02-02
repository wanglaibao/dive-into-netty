package com.laibao.netty.flash;

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
            Selector bossSelector = Selector.open();

            Selector workerSelector = Selector.open();


            new Thread(() -> {
                try {
                    /**
                     * 对应BIO编程中服务端启动
                     */
                    ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                    listenerChannel.socket().bind(new InetSocketAddress(8001));
                    listenerChannel.configureBlocking(false);
                    listenerChannel.register(bossSelector, SelectionKey.OP_ACCEPT);

                    while (true) {
                        /**
                         * 监测是否有新的连接,这里的1指的是阻塞的时间为 1ms
                         */
                        if (bossSelector.select(1) > 0) {
                            Set<SelectionKey> set = bossSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();
                            while (keyIterator.hasNext()) {
                                SelectionKey selectionKey = keyIterator.next();
                                System.out.println();
                                if (selectionKey.isAcceptable()) {
                                    try {
                                        System.out.println();
                                        /**
                                         * 每来一个新连接,不需要创建一个线程,而是直接注册到workerSelector
                                         */
                                        SocketChannel clientChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
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



            new Thread(() -> {
                try {
                    while (true) {
                        System.out.println();
                        /**
                         * 批量轮询是否有哪些连接有数据可读,这里的1指的是阻塞的时间为 1ms
                         */
                        if (workerSelector.select(1) > 0) {
                            System.out.println();
                            Set<SelectionKey> set = workerSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();

                            while (keyIterator.hasNext()) {
                                SelectionKey selectionKey = keyIterator.next();
                                System.out.println();
                                if (selectionKey.isReadable()) {
                                    System.out.println();
                                    try {
                                        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                        System.out.println();
                                        /**
                                         *  面向 Buffer
                                         */
                                        clientChannel.read(byteBuffer);
                                        byteBuffer.flip();
                                        System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer).toString());
                                    } finally {
                                        keyIterator.remove();
                                        selectionKey.interestOps(SelectionKey.OP_READ);
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
