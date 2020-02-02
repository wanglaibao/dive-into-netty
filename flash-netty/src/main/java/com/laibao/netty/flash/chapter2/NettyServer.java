package com.laibao.netty.flash.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;



public class NettyServer {

    /**
     * 1: 首先创建了两个NioEventLoopGroup,这两个对象可以看做是传统IO编程模型的两大线程组,
     *    bossGroup表示监听端口,accept新连接的线程组,
     *    workerGroup表示处理每一条连接的数据读写的线程组.
     *
     * 2: 接下来创建了一个引导类ServerBootstrap,这个类将引导我们进行服务端的启动工作,
     *    通过.group(bossGroup, workerGroup)给引导类配置两大线程组,这个引导类的线程模型也就定型了;
     *    通过.channel(NioServerSocketChannel.class)来指定服务端的IO模型为NIO;
     *
     *    通过.childHandler()方法,给这个引导类创建一个ChannelInitializer,这里主要就是定义后续每条连接的数据读写,业务处理逻辑;
     *    ChannelInitializer这个类中有一个泛型参数NioSocketChannel,这个类就是Netty对NIO类型的连接的抽象,
     *    NioServerSocketChannel也是对NIO类型的连接的抽象,
     *    NioServerSocketChannel和NioSocketChannel的概念可以和BIO编程模型中的ServerSocket以及Socket两个概念对应上;
     *
     * 3: 总结一下:要启动一个Netty服务端,必须要指定三类属性,分别是线程模型、IO模型、连接读写处理逻辑,有了这三者之后再调用bind(8001),
     *    我们就可以在本地绑定一个8001端口启动起来.
     *
     *
     */

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel socketChannel) {
                        System.out.println("hello,jinGe");
                    }
                });

        //serverBootstrap.bind(8001);

        serverBootstrap.bind(8001).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    System.out.println("端口绑定成功!");
                } else {
                    System.err.println("端口绑定失败!");
                }
            }
        });

    }
}
