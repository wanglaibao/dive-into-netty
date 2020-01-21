package com.laibao.nio.test;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest8 {

    @Test
    public void testFileReadAndWrite() throws Exception {

        try(FileInputStream inputStream = new FileInputStream("input2.txt");
            FileOutputStream outputStream = new FileOutputStream("output2.txt")) {

            FileChannel inputChannel = inputStream.getChannel();
            FileChannel outputChannel = outputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocateDirect(512);

            while(true) {
                buffer.clear();

                int read = inputChannel.read(buffer);

                System.out.println("read: " + read);

                if(-1 == read) {
                    break;
                }

                buffer.flip();

                outputChannel.write(buffer);
            }

        }
    }
}
