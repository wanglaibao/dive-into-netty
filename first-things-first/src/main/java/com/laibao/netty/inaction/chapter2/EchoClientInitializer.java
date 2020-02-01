package com.laibao.netty.inaction.chapter2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class EchoClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        final EchoClientHandler clientHandler = new EchoClientHandler();
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("clientHandler",clientHandler);
    }
}
