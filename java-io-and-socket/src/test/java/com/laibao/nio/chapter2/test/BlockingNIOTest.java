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

/**
 * 一、使用 NIO 完成网络通信的三个核心：
 * 
 * 1. 通道（Channel）：负责连接
 * 		
 * 	   java.nio.channels.Channel 接口：
 * 			|--SelectableChannel
 * 				|--SocketChannel
 * 				|--ServerSocketChannel
 * 				|--DatagramChannel
 * 
 * 				|--Pipe.SinkChannel
 * 				|--Pipe.SourceChannel
 * 
 * 2. 缓冲区（Buffer）：负责数据的存取
 * 
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控SelectableChannel通道的IO状况
 *
 * BlockingNIOTest 采用的是 阻塞的方式 实现客户端服务端网络通信
 */
public class BlockingNIOTest {

	/**
	 * 客户端
	 * 读取本地图片 1.jpg 并且通过SocketChannel方式发送到服务端去
	 * @throws IOException
	 */
	@Test
	public void testBlockClient() throws IOException{
		//1. 获取通道
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		
		FileChannel fileChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		
		//2. 分配指定大小的缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		//3. 读取本地文件，并发送到服务端
		while(fileChannel.read(byteBuffer) != -1){
			byteBuffer.flip();
			socketChannel.write(byteBuffer);
			byteBuffer.clear();
		}
		
		//4. 关闭通道
		fileChannel.close();
		socketChannel.close();
	}
	
	/**
	 * 服务端
	 * 读取客户端提交的数据,并且保存到服务端，生成文件2.jpg
	 * @throws IOException
	 */
	@Test
	public void testBlockServer() throws IOException{
		//1. 获取通道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		
		FileChannel fileChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		
		//2. 绑定连接
		serverSocketChannel.bind(new InetSocketAddress(9898));
		
		//3. 获取客户端连接的通道
		SocketChannel socketChannel = serverSocketChannel.accept();
		
		//4. 分配指定大小的缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		//5. 接收客户端的数据，并保存到本地
		while(socketChannel.read(byteBuffer) != -1){
			byteBuffer.flip();
			fileChannel.write(byteBuffer);
			byteBuffer.clear();
		}
		
		//6. 关闭通道
		socketChannel.close();
		fileChannel.close();
		serverSocketChannel.close();
		
	}
	
}
