package com.yuce.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;

class TestSocket2 {
	
	
	// 阻塞式,客户端发信息, 服务端有反馈
	@Test
	void client() throws IOException {
		// 1. 创建通道
		
		SocketChannel sCh = SocketChannel.open(new InetSocketAddress("127.0.0.1", 3388));
		
		// 2. 缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		
		// 3. 传输 
		while(inChannel.read(buf) != -1) {
			
			buf.flip();
			sCh.write(buf);
			
			buf.clear();
		}
		
		// 表明已发完信息
		sCh.shutdownOutput();
		
		// 4. 接收服务端反馈
		int len;
		while((len = sCh.read(buf)) != -1) {
			buf.flip();
			System.out.println("收到来自 Server 的信息:" + new String(buf.array(), 0, len));
			buf.clear();
		}
		sCh.close();
		inChannel.close();
		
	}

	@Test
	void server() throws IOException {
		
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		
		ssChannel.bind(new InetSocketAddress(3388));
		
		SocketChannel sCh = ssChannel.accept();
		
		FileChannel outCh = FileChannel.open(Paths.get("5.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		while(sCh.read(buf) != -1) {
			buf.flip();
			outCh.write(buf);
			buf.clear();
		}
		
		String str = "hello client";
		
		buf.put(str.getBytes());
		buf.flip();
		sCh.write(buf);
		
		sCh.close();
		outCh.close();
		ssChannel.close();
		
	}
}
