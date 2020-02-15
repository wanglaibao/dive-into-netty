package com.laibao.netty.inaction.chapter1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *
 *
 * 被回调触发的 ChannelHandler
 */
public class ConnectHandler extends ChannelInboundHandlerAdapter {

    @Override
    //当一个新的连接已经被建立时，channelActive(ChannelHandlerContext)将会被调用
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("DecoratorClient " + ctx.channel().remoteAddress() + " connected");
    }
}
