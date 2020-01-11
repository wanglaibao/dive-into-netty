package com.laibao.netty.firstexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 该类实现了ChannelInboundHandler接口,表示处理进入的请求
 * 如果实现了ChannelOutboundHandler接口,表示处理出去的请求
 */
public class HttpWorldServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println(msg.getClass());
        System.out.println(ctx.channel().remoteAddress());

        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest)msg;

            System.out.println("请求方法名：" + httpRequest.method().name());

            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求favicon.ico");
                return;
            }


            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);

            /**
             * 构造一个http响应,HttpVersion.HTTP_1_1:采用http1.1协议，HttpResponseStatus.OK：状态码200
             */
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            System.out.println("channel do business processing  444444444444444");

            /**
             * 如果只是调用write方法，他仅仅是存在缓冲区里，并不会返回客户端
             * 调用writeAndFlush可以
             */
            ctx.writeAndFlush(response);
            //ctx.channel().close();
        }
    }


    /**
     * 下列各个方法的调用顺序如下：
     *          handlerAdded
     *          channelRegistered
     *          channelActive
     *          请求方法名:GET【channelRead0】
     *          【下面的表示的是断开连接后】
     *              1.如果是使用curl ：连接会立刻关闭
     *              2.如果是浏览器访问，http1.0：它是短连接，会立刻关闭。http1.1，是长连接，连接保持一段时间
     *          channelInactive
     *          channelUnregistered
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        System.out.println("handler added  11111111111111");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("channel registered  22222222222222");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channel active  33333333333333333");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channel inactive  555555555555555555");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("channel unregistered   66666666666666666666");
    }
}
