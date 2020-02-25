package com.laibao.netty.fight.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PooledServer {

    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private Selector selector;

    /**
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        PooledServer server = new PooledServer();
        server.initServer(8000);
        server.listen();
    }

    /**
     *
     * @param port
     * @throws IOException
     */
    public void initServer(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        this.selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务端启动成功！");
    }

    /**
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        /**
         * selector轮询
         */
        while (true) {
            selector.select();
            Iterator iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(this.selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    selectionKey.interestOps(selectionKey.interestOps()&(~SelectionKey.OP_READ));
                    threadPool.execute(new HandlerChannelThread(selectionKey));
                }
            }
        }
    }
}

class HandlerChannelThread extends Thread{

    private SelectionKey selectionKey;

    HandlerChannelThread(SelectionKey selectionKey){
        this.selectionKey = selectionKey;
    }


    @Override
    public void run() {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            /*
            int size = 0;
            while ((size = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                baos.write(buffer.array(),0,size);
                buffer.clear();
            }*/

            int size;
            while ((size = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                baos.write(buffer.array(),0,size);
                buffer.clear();
            }
            baos.close();

            //
            byte[] content=baos.toByteArray();
            ByteBuffer writeBuf = ByteBuffer.allocate(content.length);
            writeBuf.put(content);
            writeBuf.flip();
            socketChannel.write(writeBuf);//
            if(size == -1){
                socketChannel.close();
            }else{
                selectionKey.interestOps(selectionKey.interestOps()|SelectionKey.OP_READ);
                selectionKey.selector().wakeup();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
