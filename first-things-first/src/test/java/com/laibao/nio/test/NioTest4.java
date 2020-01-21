package com.laibao.nio.test;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest4 {

    private static final String INPUT_FILE_PATH = "input.txt";

    private static final String OUTPUT_FILE_PATH = "output.txt";

    @Test
    public void testReadFromOneFileAndWriteToAnotherFile() throws IOException {


        try(FileInputStream inputStream = new FileInputStream(INPUT_FILE_PATH);
            FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE_PATH)) {
            FileChannel inputChannel = inputStream.getChannel();
            FileChannel outputChannel = outputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(512);

            while(true) {
                buffer.clear(); // 如果注释掉该行代码会发生什么情况？

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
