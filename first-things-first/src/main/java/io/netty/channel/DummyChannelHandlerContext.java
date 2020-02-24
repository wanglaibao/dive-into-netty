package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

public class DummyChannelHandlerContext extends AbstractChannelHandlerContext{

    public static ChannelHandlerContext DUMMY_INSTANCE = new DummyChannelHandlerContext(
            null,
            null,
            "dummyChannelHandlerContext"
    );

    public DummyChannelHandlerContext(DefaultChannelPipeline pipeline,
                                      EventExecutor executor,
                                      String name) {

        super(pipeline, executor, name, true,false);
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }
}
