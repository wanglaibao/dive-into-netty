package com.laibao.netty.inaction.chapter4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 写出到 Channel
 *
 * 从多个线程使用同一个 Channel
 *
 */
public class ChannelOperationExample {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * 写出到 Channel
     */
    public static void writingToChannel() {

        // Get the channel reference from somewhere
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        //创建持有要写数据的 ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);
        ChannelFuture channelFuture = channel.writeAndFlush(byteBuf);

        //添加 ChannelFutureListener 以便在写操作完成后接收通知
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                //写操作完成，并且没有错误发生
                if (future.isSuccess()) {
                    System.out.println("Write successful");
                } else {
                    //记录错误
                    System.err.println("Write error");
                    future.cause().printStackTrace();
                }
            }
        });
    }



    /**
     * 从多个线程使用同一个 Channel
     */
    public static void writingToChannelFromManyThreads() {
        // Get the channel reference from somewhere
        final Channel channel = CHANNEL_FROM_SOMEWHERE;

        //创建持有要写数据的ByteBuf
        final ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);

        //创建将数据写到Channel 的 Runnable
        Runnable writer = new Runnable() {
            @Override
            public void run() {
                channel.write(buf.duplicate());
            }
        };

        //获取到线程池Executor的引用
        Executor executor = Executors.newCachedThreadPool();

        //递交写任务给线程池以便在某个线程中执行
        // write in one thread
        executor.execute(writer);

        //递交另一个写任务以便在另一个线程中执行
        // write in another thread
        executor.execute(writer);
        //...
    }

}
