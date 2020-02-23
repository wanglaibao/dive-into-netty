package com.laibao.netty.inaction.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 *  消费并释放入站消息
 *
 *  扩展了ChannelInboundandlerAdapter
 *
 */
@Sharable
public class DiscardInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //通过调用 ReferenceCountUtil.release()方法释放资源
        ReferenceCountUtil.release(msg);
    }
}
