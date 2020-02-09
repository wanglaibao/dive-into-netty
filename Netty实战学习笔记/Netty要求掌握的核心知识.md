##      Netty要求掌握的核心知识


* IO操作的底层原理
* IO多路复用器模型
* NIO零拷贝原理

* NIO

    Selector, SelectionKey,Channel,ByteBuffer

* Reactor设计模式
* Netty线程模型
* Netty自身的零拷贝如何改进了NIO的零拷贝
* Netty异步模型【Future  ChannelFuture  Callable  ChannelHandler】
* Netty  EventLoop 如何自定义任务队列【taskQueue】


```
        //比如这里我们有一个非常耗时长的业务-> 异步执行 -> 提交该channel 对应的
        //NIOEventLoop 的 taskQueue中,

        //解决方案1: 用户程序自定义的普通任务

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 金戈", CharsetUtil.UTF_8));
                    System.out.println("channel code=" + ctx.channel().hashCode());
                } catch (Exception ex) {
                    System.out.println("发生异常" + ex.getMessage());
                }
            }
        });



         //解决方案2 : 用户自定义定时任务 -> 将该任务提交到scheduleTaskQueue中

            ctx.channel().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(5 * 1000);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,金戈", CharsetUtil.UTF_8));
                        System.out.println("channel code=" + ctx.channel().hashCode());
                    } catch (Exception ex) {
                        System.out.println("发生异常" + ex.getMessage());
                    }
                }
            }, 5, TimeUnit.SECONDS);
```

* Netty  future-listener机制
* Netty心跳机制的实现原理
* Netty编解码机制的实现原理
* Netty的粘包拆包原理
* Netty入站和出站机制
* ChannelHandler的链式调用机制


## Netty核心组件

* 服务引导启动类
    Bootstrap ServerBootstrap AbstractBootstrap

* 网络传输模型
    Channel SocketChannel ServerSocketChannel
    NioSocketChannel NioServerSocketChannel
    OioSocketChannel OioServerSocketChannel
    EPollSocketChannel EPollServerSocketChannel

* IO多路复用选择器
    Selector

* 数据容器【零拷贝】

     ByteBuffer

* 事件处理器
    ChannelHandler

* 线程池以及线程模型
    EventLoop,NioEventLoop
    EventLoopGroup,NioEventLoopGroup

* 异步模型【事件回调机制】
   Future,ChannelFuture
   Promise,ChannelPromise

* 事件监听器
  GenericFutureListener
  ChannelFutureListener

* 链式管道
    PipeLine
    ChannelPipeLine







