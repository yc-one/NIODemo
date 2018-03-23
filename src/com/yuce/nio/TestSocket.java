package com.yuce.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class TestSocket {
	
	
	// 阻塞式 IO 
	@Test
	public void client() throws IOException {
		// 1.获取通道
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 3388));
		
		
		FileChannel fileCh = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		
		// 2.创建缓冲区
		ByteBuffer bf = ByteBuffer.allocate(1024);
		
		// 3.读取本地文件,并发送到服务器 
		while(fileCh.read(bf) != -1) {
			bf.flip();
			socketChannel.write(bf);
			bf.clear();
		}
		
		socketChannel.close();
		fileCh.close();
		
	}
	
	@Test
	public void server() throws IOException {
		
		// 1.创建通道
		ServerSocketChannel ssCh = ServerSocketChannel.open();
		
		FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		
		// 2.绑定端口
		ssCh.bind(new InetSocketAddress(3388));
		
		// 3.获取客户端连接的通道
		SocketChannel sCh = ssCh.accept();
		
		// 4.创建缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		while(sCh.read(buffer) != -1) {
			buffer.flip();
			outChannel.write(buffer);
			buffer.clear();
		}
		
		outChannel.close();
		sCh.close();
		ssCh.close();
		 
		
	}
	
}
