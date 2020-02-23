package com.laibao.netty.inaction.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 使用SimpleChannelInboundHandler
 *
 * 扩展了SimpleChannelInboundHandler
 *
 */
@Sharable
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {
        //不需要任何显式的资源释放
        // No need to do anything special
    }
}
