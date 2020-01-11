package com.laibao.netty.firstexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloWorldServer {

    public static void main(String[] args) {

        /**
         * 定义两个事件循环组，就是死循环
         */
        EventLoopGroup bossGroup = null;
        EventLoopGroup workerGroup = null;
        try {

            /**
             * 仅仅接受连接,转给workerGroup,自己不做处理
             */
            bossGroup = new NioEventLoopGroup();

            /**
             * 处理客户端的连接请求以及IO读写操作
             */
            workerGroup = new NioEventLoopGroup();

            /**
             * 服务端引导程序 用于配置启动以及监听客户端的连接
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();


            /**
             * childHandler子处理器,传入一个初始化器【HelloWorldServerInitializer】
             */
            serverBootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new HelloWorldServerInitializer());

            /**
             * 绑定一个端口并且同步，生成一个ChannelFuture对象
             */
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();

            /**
             * 对关闭的监听
             */
            channelFuture.channel().closeFuture().sync();

        }catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            /**
             *  循环组优雅关闭
             */
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
