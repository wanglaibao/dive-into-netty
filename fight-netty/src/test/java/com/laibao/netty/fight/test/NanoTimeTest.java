package com.laibao.netty.fight.test;

import org.junit.Test;

public class NanoTimeTest {

    @Test
    public  void testNanoTime(){
        try {
            System.out.println(System.nanoTime()); //纳秒  10亿分之1
            Thread.sleep(1000);
            System.out.println(System.nanoTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
