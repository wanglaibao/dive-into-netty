package com.laibao.netty.fight.simplehttpserver;

import com.atguigu.netty.http.SimpleHttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class SimpleHttpServerInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //向管道加入处理器
        //加入一个netty 提供的httpServerCodec codec =>[coder - decoder]
        //HttpServerCodec 说明
        //1. HttpServerCodec 是netty 提供的处理http的 编-解码器
        pipeline.addLast("httpServerCodec",new HttpServerCodec());

        //2. 增加一个自定义的handler
        pipeline.addLast("simpleHttpServerHandler", new SimpleHttpServerHandler());

        System.out.println("ok~~~~");

    }
}
