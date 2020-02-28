package com.laibao.netty.designpattern.futurelistener;

public class ThreadJoinExample {

    public static final int SLEEP_GAP = 1000;

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    static class WateringThread extends Thread {

        public WateringThread() {
            super("** 烧水-Thread");
        }

        public void run() {
            try {
                System.out.println("洗好水壶");
                System.out.println("灌上凉水");
                System.out.println("放在火上");

                //线程睡眠一段时间，代表烧水中
                Thread.sleep(SLEEP_GAP);
                System.out.println("水开了");

            } catch (Exception e) {
                System.out.println(" 发生异常被中断.");
            }
            System.out.println(" 运行结束.");
        }

    }

    static class WashingThread extends Thread {

        public WashingThread() {
            super("$$ 清洗-Thread");
        }

        public void run() {
            try {
                System.out.println("洗茶壶");
                System.out.println("洗茶杯");
                System.out.println("拿茶叶");
                //线程睡眠一段时间，代表清洗中
                Thread.sleep(SLEEP_GAP);
                System.out.println("洗完了");

            } catch (Exception e) {
                System.out.println(" 发生异常被中断.");
            }
            System.out.println(" 运行结束.");
        }

    }


    public static void main(String args[]) {
        Thread waterThread = new WateringThread();
        Thread washThread = new WashingThread();

        waterThread.start();
        washThread.start();
        try {
            // 合并烧水-线程
            waterThread.join();
            // 合并清洗-线程
            washThread.join();

            Thread.currentThread().setName("主线程");
            System.out.println("泡茶喝");

        } catch (Exception ex) {
            System.out.println(getCurThreadName() + "发生异常被中断.");
        }
        System.out.println(getCurThreadName() + " 运行结束.");
    }
}