package com.laibao.netty.inaction.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 *  Blocking networking with Netty
 *
 */
public class NettyOioServer {

    public void server(int port){

        final ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));

        EventLoopGroup group = new OioEventLoopGroup();

        try {
            //创建 ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(group)
                    //使用 OioEventLoopGroup以允许阻塞模式（旧的I/O）
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //指定 ChannelInitializer，对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    //添加一个 ChannelInboundHandlerAdapter以拦截和处理事件
                                    new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ctx.writeAndFlush(byteBuf.duplicate())
                                               .addListener(
                                                            //将消息写到客户端，并添加 ChannelFutureListener，
                                                            //以便消息一被写完就关闭连接
                                                            ChannelFutureListener.CLOSE);
                                        }
                                    });
                        }
                    });

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
