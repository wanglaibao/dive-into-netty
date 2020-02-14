package com.laibao.nio.chapter2.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class BlockingNIO2Test {
	
	/**
	 * 客户端
	 * @throws IOException
	 */
	@Test
	public void testClient() throws IOException{

		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		
		FileChannel fileChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		while(fileChannel.read(buf) != -1){
			buf.flip();
			socketChannel.write(buf);
			buf.clear();
		}
		
		socketChannel.shutdownOutput();
		
		//接收服务端的反馈
		int len = 0;
		while((len = socketChannel.read(buf)) != -1){
			buf.flip();
			System.out.println(new String(buf.array(), 0, len));
			buf.clear();
		}
		
		fileChannel.close();
		socketChannel.close();
	}
	
	/**
	 * 服务端
	 * @throws IOException
	 */
	@Test
	public void testServer() throws IOException{

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		
		FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		
		serverSocketChannel.bind(new InetSocketAddress(9898));
		
		SocketChannel socketChannel = serverSocketChannel.accept();
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		while(socketChannel.read(buf) != -1){
			buf.flip();
			outChannel.write(buf);
			buf.clear();
		}
		
		//发送反馈给客户端
		buf.put("服务端接收数据成功".getBytes());
		buf.flip();
		socketChannel.write(buf);
		
		socketChannel.close();
		outChannel.close();
		serverSocketChannel.close();
	}

}
