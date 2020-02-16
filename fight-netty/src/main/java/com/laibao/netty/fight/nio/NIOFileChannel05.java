package com.laibao.netty.fight.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel05 {

    public static void main(String[] args){

        try(
                FileInputStream fileInputStream = new FileInputStream("d:\\file01.txt");
                FileChannel fileChannel01 = fileInputStream.getChannel();

                FileOutputStream fileOutputStream = new FileOutputStream("d:\\file02.txt");
                FileChannel fileChannel02 = fileOutputStream.getChannel()

        ){
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            while (fileChannel01.read(byteBuffer) != -1){
                byteBuffer.flip();
                fileChannel02.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
