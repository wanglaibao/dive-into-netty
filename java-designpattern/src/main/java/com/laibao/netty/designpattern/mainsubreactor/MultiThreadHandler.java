package com.laibao.netty.designpattern.mainsubreactor;



import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MultiThreadHandler implements Runnable {

    private final SocketChannel socketChannel;

    private final SelectionKey selectionKey;

    private ByteBuffer input = ByteBuffer.allocate(1024);

    private ByteBuffer output = ByteBuffer.allocate(1024);

    private static final int READING = 0, SENDING = 1;

    private int state = READING;

    private ExecutorService threadPool = Executors.newFixedThreadPool(2);

    static final int PROCESSING = 3;

    public MultiThreadHandler(Selector selector, SocketChannel channel) throws IOException {
        socketChannel = channel;
        socketChannel.configureBlocking(false);
        // Optionally try first read now
        selectionKey = socketChannel.register(selector, 0);
        //将Handler作为callback对象
        selectionKey.attach(this);
        //第二步,注册Read就绪事件
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    private boolean inputIsComplete() {
       /* ... */
        return true;
    }

    private boolean outputIsComplete() {

       /* ... */
        return true;
    }

    private void process() {
       /* ... */
        return;
    }

    public void run()
    {
        try
        {
            if (state == READING)
            {
                read();
            }
            else if (state == SENDING)
            {
                send();
            }
        } catch (IOException ex) {
            /* ... */
            ex.printStackTrace();
        }
    }

    private synchronized void read() throws IOException {
        // ...
        socketChannel.read(input);
        if (inputIsComplete()) {
            state = PROCESSING;
            //使用线程pool异步执行
            threadPool.execute(new Processor());
        }
    }

    private void send() throws IOException {
        socketChannel.write(output);

        //write完就结束了, 关闭select key
        if (outputIsComplete())
        {
            selectionKey.cancel();
        }
    }

    private synchronized void processAndHandOff() {
        process();
        state = SENDING;
        // or rebind attachment
        //process完,开始等待write就绪
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    private class Processor implements Runnable {
        public void run()
        {
            processAndHandOff();
        }
    }

}

