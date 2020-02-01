package com.laibao.netty.inaction.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * EchoServerHandler
 *
 */

//EchoServerHandler 被标注为@Shareable，表示一个ChannelHandler可以被多个Channel安全地共享
//所以我们可以总是使用同样的实例,这里对于所有的客户端连接来说,都会使用同一个EchoServerHandler,因为其被标注为@Sharable，
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        //将消息记录到控制台
        System.out.println("Server received: " + byteBuf.toString(CharsetUtil.UTF_8));

        //将接收到的消息写给发送者，而不冲刷出站消息
        ctx.write(byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //将未决消息冲刷到远程节点，并且关闭该 Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //打印异常栈跟踪
        cause.printStackTrace();
        //关闭该Channel
        ctx.close();
    }
}
