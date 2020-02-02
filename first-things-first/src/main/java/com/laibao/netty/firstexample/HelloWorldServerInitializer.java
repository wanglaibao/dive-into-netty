package com.laibao.netty.firstexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HelloWorldServerInitializer extends ChannelInitializer<NioSocketChannel> {

    /**
     * 这是一个回调方法,在channel被注册时调用
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
        /**
         * 管道链,可以在它里面添加进很多ChannelHandler
         */
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        /**
         * 添加一个Netty处理器 HttpServerCodec 完成http编解码
         *
         */
        channelPipeline.addLast("httpServerCodec", new HttpServerCodec());

        /**
         * 增加一个自己定义的处理器 HttpWorldServerHandler 用来处理真正的业务逻辑
         */
        channelPipeline.addLast("testHttpServerHandler", new HttpWorldServerHandler());
    }

}
