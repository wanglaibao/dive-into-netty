package com.laibao.netty.fight.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 *  BIO 服务端示例
 *
 *  可以通过Telnet命令来进行测试
 *
 *  telnet 127.0.0.1 9898
 *
 *      Ctrl + ]
 *
 *      send hello,jinGe
 *
 *      serverSocket.accept()  阻塞方法
 *
 *      inputStream.read(bytes) 阻塞方法
 *
 *      inputStream.write("ddddd") 阻塞方法
 */
public class BIOServer {

    public static void main(String[] args) throws Exception {

        //线程池机制

        //思路
        //1. 创建一个线程池
        //2. 如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)

        //ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        int theadsNumber = Runtime.getRuntime().availableProcessors();

        ExecutorService threadPool = new ThreadPoolExecutor(theadsNumber,theadsNumber,0, TimeUnit.SECONDS,new ArrayBlockingQueue(10));

        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(9898);

        System.out.println("服务器启动了");

        while (true) {

            System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
            //监听，等待客户端连接
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //就创建一个线程，与之通讯(单独写一个方法)
            /**
                newCachedThreadPool.execute(new Runnable() {
                    public void run() { //我们重写
                        //可以和客户端通讯
                        handler(socket);
                    }
                });
            */

            threadPool.execute(() -> handler(socket));

        }


    }

    //编写一个handler方法，和客户端通讯
    public static void handler(Socket socket) {

        try {
            System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();

            /**  其实这样写应该更简洁一点
                int read0;
                while ((read0 =  inputStream.read(bytes)) != -1) {
                    System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
                    System.out.println("read....");
                    System.out.println(new String(bytes, 0, read0)); //输出客户端发送的数据
            }*/

            //循环的读取客户端发送的数据
            while (true) {

                System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());

                System.out.println("read....");
               int read =  inputStream.read(bytes);
               if(read != -1) {
                   System.out.println(new String(bytes, 0, read)); //输出客户端发送的数据
               } else {
                   break;
               }
            }


        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
