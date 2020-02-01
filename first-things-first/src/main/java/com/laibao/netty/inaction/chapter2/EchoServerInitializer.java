package com.laibao.netty.inaction.chapter2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class EchoServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(serverHandler);
    }
}
