package com.laibao.netty.reactordesignpattern.multithread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Handler implements Runnable{

    private final SocketChannel socketChannel;

    private final SelectionKey selectionKey;

    private static final int READ_BUF_SIZE = 1024;

    private static final int WRiTE_BUF_SIZE = 1024;

    ByteBuffer readBuf = ByteBuffer.allocate(READ_BUF_SIZE);

    ByteBuffer writeBuf = ByteBuffer.allocate(WRiTE_BUF_SIZE);

    protected Handler(Selector selector, SocketChannel channel) throws IOException {
        socketChannel = channel;
        socketChannel.configureBlocking(false);
        /**
         * Register the socket channel with interest-set set to READ operation
         */
        selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (selectionKey.isReadable()) {
                read();
            } else if (selectionKey.isWritable()) {
                write();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private synchronized void read() throws IOException {
        int numBytes;
        try {
            numBytes = socketChannel.read(readBuf);
            System.out.println("read(): #bytes read into 'readBuf' buffer = " + numBytes);
            if (numBytes == -1) {
                selectionKey.cancel();
                socketChannel.close();
                System.out.println("read(): client connection might have been dropped!");
            } else {
                Reactor.workerPool.execute(
                        /*
                        new Runnable() {
                            public void run() {
                                process();
                            }
                        }*/

                        () -> process()
                );
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }


    private synchronized void write() throws IOException {
        int numBytes = 0;

        try {
            numBytes = socketChannel.write(writeBuf);
            System.out.println("write(): #bytes read from 'writeBuf' buffer = " + numBytes);

            if (numBytes > 0) {
                readBuf.clear();
                writeBuf.clear();

                /**
                 * Set the key's interest-set back to READ operation
                 */
                selectionKey.interestOps(SelectionKey.OP_READ);
                selectionKey.selector().wakeup();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Process data by echoing input to output
     */
    private synchronized void process() {
        byte[] bytes;
        readBuf.flip();
        bytes = new byte[readBuf.remaining()];
        readBuf.get(bytes, 0, bytes.length);
        System.out.print("process(): " + new String(bytes, Charset.forName("UTF-8")));

        writeBuf = ByteBuffer.wrap(bytes);
        /**
         * Set the key's interest to WRITE operation
         */
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        selectionKey.selector().wakeup();
    }

}
