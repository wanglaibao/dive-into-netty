package com.laibao.netty.fight.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8888));
        serverSocketChannel.configureBlocking(false);

        System.out.println("server started, listening on :" + serverSocketChannel.getLocalAddress());
        Selector selector = Selector.open();
        /**
         * 注册连接接收事件
         */
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                handleSelectionKey(selectionKey);
            }
        }

    }


    private static void handleSelectionKey(SelectionKey selectionKey) {
        if(selectionKey.isAcceptable()) {
            try {
                ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                /**
                 * 注册读取事件
                 */
                sc.register(selectionKey.selector(), SelectionKey.OP_READ );
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        } else if (selectionKey.isReadable()) { //flip
            SocketChannel socketChannel = null;
            try {
                socketChannel = (SocketChannel)selectionKey.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.clear();
                int len = socketChannel.read(buffer);

                if(len != -1) {
                    System.out.println(new String(buffer.array(), 0, len));
                }

                ByteBuffer bufferToWrite = ByteBuffer.wrap("HelloClient".getBytes());
                socketChannel.write(bufferToWrite);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(socketChannel != null) {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
