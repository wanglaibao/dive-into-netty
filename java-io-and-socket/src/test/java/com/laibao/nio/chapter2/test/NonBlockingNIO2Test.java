package com.laibao.nio.chapter2.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * 模拟UDP协议的发送接收测试
 */
public class NonBlockingNIO2Test {
	
	@Test
	public void testSend() throws IOException{

		DatagramChannel datagramChannel = DatagramChannel.open();
		
		datagramChannel.configureBlocking(false);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		Scanner scan = new Scanner(System.in);
		
		while(scan.hasNext()){
			String str = scan.next();
			buf.put((new Date().toString() + ":\n" + str).getBytes());
			buf.flip();
			datagramChannel.send(buf, new InetSocketAddress("127.0.0.1", 9898));
			buf.clear();
		}
		
		datagramChannel.close();
	}
	
	@Test
	public void testReceive() throws IOException{

		DatagramChannel datagramChannel = DatagramChannel.open();
		
		datagramChannel.configureBlocking(false);
		
		datagramChannel.bind(new InetSocketAddress(9898));
		
		Selector selector = Selector.open();
		
		datagramChannel.register(selector, SelectionKey.OP_READ);
		
		while(selector.select() > 0){
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			while(it.hasNext()){
				SelectionKey sk = it.next();
				
				if(sk.isReadable()){
					ByteBuffer buf = ByteBuffer.allocate(1024);
					
					datagramChannel.receive(buf);
					buf.flip();
					System.out.println(new String(buf.array(), 0, buf.limit()));
					buf.clear();
				}
			}
			
			it.remove();
		}
	}

}
