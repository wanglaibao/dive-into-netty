package com.laibao.netty.inaction.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
/**
 * ToIntegerDecoder 类扩展了 ByteToMessageDecoder
 *
 * 扩展ByteToMessageDecoder类，以将字节解码为特定的格式
 *
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        /**
         *  检查是否至少有4字节可读（一个 int 的字节长度）
         */
        if (in.readableBytes() >= 4) {
            /**
             * 从入站 ByteBuf 中读取一个 int，并将其添加到解码消息的 List 中
             */
            out.add(in.readInt());
        }
    }
}