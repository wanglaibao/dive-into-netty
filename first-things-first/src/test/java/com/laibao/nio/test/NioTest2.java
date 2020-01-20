package com.laibao.nio.test;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest2 {


    private static final String FILE_PATH = "NioTest2.txt";

    @Test
    public void testGetChannelFromInputStream() throws IOException {

        try( FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {

            FileChannel fileChannel = fileInputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            /**
             * 从文件中读数据到ByteBuffer缓冲区
             */
            fileChannel.read(byteBuffer);

            byteBuffer.flip();

            /**
             * 遍历缓冲区并且进行打印字节数据
             */
            while(byteBuffer.remaining() > 0) {
                byte b = byteBuffer.get();
                System.out.println("Character: " + (char)b);
            }

        }


    }
}
