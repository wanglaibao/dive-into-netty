package com.laibao.netty.inaction.chapter6;

import io.netty.channel.*;

/**
 * 添加 ChannelFutureListener 到 ChannelPromise
 *
 */
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg,
                      ChannelPromise promise) {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                if (!channelFuture.isSuccess()) {
                    channelFuture.cause().printStackTrace();
                    channelFuture.channel().close();
                }
            }
        });
    }
}
