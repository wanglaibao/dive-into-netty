package com.laibao.netty.designpattern.futurelistener;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskExample {

    public static final int SLEEP_GAP = 1000;

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    private static class WateringJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {

            try {
                System.out.println("洗好水壶");
                System.out.println("灌上凉水");
                System.out.println("放在火上");
                System.out.println();

                //线程睡眠一段时间，代表烧水中
                Thread.sleep(SLEEP_GAP);
                System.out.println("水开了");

            } catch (Exception e) {
                System.out.println(" 发生异常被中断.");
                return false;
            }

            System.out.println(" 运行结束.");
            return true;
        }
    }


   private static class WashingJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                System.out.println("洗茶壶");
                System.out.println("洗茶杯");
                System.out.println("拿茶叶");
                //线程睡眠一段时间，代表清洗中
                Thread.sleep(SLEEP_GAP);
                System.out.println("洗完了");
                System.out.println();

            } catch (Exception e) {
                System.out.println(" 清洗工作 发生异常被中断.");
                return false;
            }
            System.out.println(" 清洗工作  运行结束.");
            return true;
        }
    }

    private static void drinkTea(boolean waterOk, boolean cupOk) {
        if (waterOk && cupOk) {
            System.out.println("泡茶喝");
        } else if (!waterOk) {
            System.out.println("烧水失败，没有茶喝了");
        } else if (!cupOk) {
            System.out.println("杯子洗不了，没有茶喝了");
        }

    }

    public static void main(String args[]) {

        Callable<Boolean> wateringJob = new WateringJob();
        FutureTask<Boolean> wateringTask = new FutureTask<>(wateringJob);
        Thread hThread = new Thread(wateringTask, "** 烧水-Thread");

        Callable<Boolean> washingJob = new WashingJob();
        FutureTask<Boolean> washingTask = new FutureTask<>(washingJob);
        Thread wThread = new Thread(washingTask, "$$ 清洗-Thread");

        hThread.start();
        wThread.start();
        Thread.currentThread().setName("主线程");

        try {

            boolean  waterOk = wateringTask.get();
            boolean  washOk = washingTask.get();

            drinkTea(waterOk, washOk);

        } catch (Exception e) {
            System.out.println(getCurThreadName() + "发生异常被中断.");
        }

        System.out.println(getCurThreadName() + " 运行结束.");
    }
}