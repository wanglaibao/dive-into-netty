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

    @Test
    public void testNanoTimeTwo(String[] args){
        System.out.println(System.nanoTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.nanoTime());

        int a = 10;
        int b = 20;
        int c = 10;
        c -= a - b; // c = c - (a-b) = c - a + b = 10 - 10 + 20
        System.out.println(c);
    }
}
