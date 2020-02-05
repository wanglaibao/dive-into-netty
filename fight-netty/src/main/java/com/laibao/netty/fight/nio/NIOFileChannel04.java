package com.laibao.netty.fight.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {

    public static void main(String[] args){

        try(
            //创建相关流
            FileInputStream fileInputStream = new FileInputStream("d:\\Koala.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream("d:\\Koala_copy.jpg");

            //获取各个流对应的filechannel
            FileChannel sourceCh = fileInputStream.getChannel();
            FileChannel destCh = fileOutputStream.getChannel()
        ){
            //使用transferForm完成拷贝
            destCh.transferFrom(sourceCh,0,sourceCh.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
