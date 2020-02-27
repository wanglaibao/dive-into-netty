package com.laibao.netty.designpattern.singlethreadreactor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

class Handler implements Runnable {

    private final SocketChannel socketChannel;

    private final SelectionKey selectionKey;

    private final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private EventState state = EventState.READING;

    Handler(Selector selector, SocketChannel channel) throws IOException {
        socketChannel = channel;
        socketChannel.configureBlocking(false);
        /**
         * 注册感兴趣的IO事件
         */
        selectionKey = socketChannel.register(selector, 0);
        /**
         * 将Handler作为选择键的附件
         */
        selectionKey.attach(this);
        /**
         * 注册Read就绪事件
         */
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    public void run() {
        try {
            if (state == EventState.WRITING) {
                //写入通道
                socketChannel.write(byteBuffer);
                //写完后,准备开始从通道读,byteBuffer切换成写模式
                byteBuffer.clear();
                //写完后,注册read就绪事件
                selectionKey.interestOps(SelectionKey.OP_READ);
                //写完后,进入接收的状态
                state = EventState.READING;
            } else if (state == EventState.READING) {
                //从通道读
                int length = -1;
                while ((length = socketChannel.read(byteBuffer)) > 0) {
                    new String(byteBuffer.array(), 0, length);
                }
                //读完后，准备开始写入通道,byteBuffer切换成读模式
                byteBuffer.flip();
                //读完后，注册write就绪事件
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                //读完后,进入发送的状态
                state = EventState.WRITING;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}

