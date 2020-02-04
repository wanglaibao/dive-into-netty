package com.laibao.netty.fight.test;

import io.netty.util.NettyRuntime;
import org.junit.Test;

public class AvailableProcessorsTest {


    @Test
    public void testAvailableProcessors() {
        final int availableProcessors = NettyRuntime.availableProcessors();

        final int runtimeAvailableProcessors = Runtime.getRuntime().availableProcessors();

        System.out.println("availableProcessors is "+availableProcessors);

        System.out.println("runtimeAvailableProcessors is "+runtimeAvailableProcessors);
    }
}
