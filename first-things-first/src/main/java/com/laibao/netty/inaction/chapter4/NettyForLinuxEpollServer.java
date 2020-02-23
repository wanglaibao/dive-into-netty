package com.laibao.netty.inaction.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class NettyForLinuxEpollServer {

    public void server(int port) {

        final ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));

        //为非阻塞模式使用NioEventLoopGroup
        EpollEventLoopGroup group = new EpollEventLoopGroup();

        try {
            //创建ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(group)
                    .channel(EpollServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))

                    //指定 ChannelInitializer，对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                                      @Override
                                      public void initChannel(NioSocketChannel ch)
                                              throws Exception {
                                          ch.pipeline().addLast(
                                                  //添加 ChannelInboundHandlerAdapter以接收和处理事件
                                                  new ChannelInboundHandlerAdapter() {
                                                      @Override
                                                      public void channelActive(
                                                              //将消息写到客户端，并添加ChannelFutureListener，
                                                              //以便消息一被写完就关闭连接
                                                              ChannelHandlerContext ctx) throws Exception {
                                                          ctx.writeAndFlush(byteBuf.duplicate())
                                                                  .addListener(
                                                                          ChannelFutureListener.CLOSE);
                                                      }
                                                  });
                                      }
                                  }
                    );

            //绑定服务器以接受连接
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            //释放所有的资源
            group.shutdownGracefully();
        }

    }
}
