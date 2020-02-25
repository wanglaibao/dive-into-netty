package com.laibao.netty.inaction.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 *
 * 扩展 ReplayingDecoder<Void> 以将字节解码为消息
 */

public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    /**
     *
     * @param ctx
     * @param in  传入的 ByteBuf 是 ReplayingDecoderByteBuf
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        /**
         * 从入站ByteBuf中读取 一个 int，并将其添加到解码消息的 List 中
         */
        out.add(in.readInt());
    }
}
