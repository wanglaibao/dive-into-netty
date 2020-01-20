package com.laibao.nio.test;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest3 {

    private static final String FILE_PATH = "NioTest3.txt";

    @Test
    public void testWriteDataToFile() throws IOException {

        try( FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH)){

            FileChannel fileChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            byte[] messages = "hello world welcome, nihao".getBytes();

            for(int i = 0; i < messages.length; ++i) {
                byteBuffer.put(messages[i]);
            }

            byteBuffer.flip();

            fileChannel.write(byteBuffer);
        }



    }
}
