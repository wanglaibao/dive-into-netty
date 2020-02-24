package com.laibao.netty.inaction.chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 缓存到ChannelHandlerContext 的引用
 *
 */
public class WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext channelHandlerContext;

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) {
        //存储到 ChannelHandlerContext的引用以供稍后使用
        this.channelHandlerContext = channelHandlerContext;
    }

    public void send(String msg) {
        //使用之前存储的到 ChannelHandlerContext的引用来发送消息
        channelHandlerContext.writeAndFlush(msg);
    }
}
