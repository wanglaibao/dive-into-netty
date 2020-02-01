package com.laibao.netty.inaction.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * EchoServer 类
 *
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args)
            throws Exception {

        //设置端口值
        int port = 8899;
        //调用服务器的 start()方法
        new EchoServer(port).start();
    }


    public void start() throws Exception {

        // 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(group)
                    // 指定所使用的 NIO 传输 Channel
                    .channel(NioServerSocketChannel.class)

                    // 使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))

                    // 添加一个EchoServerInitializer用于初始化Handler
                    .childHandler(new EchoServerInitializer());

            // 异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listening for connections on " + channelFuture.channel().localAddress());

            // 获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
            channelFuture.channel().closeFuture().sync();
        } finally {

            // 关闭 EventLoopGroup，释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
