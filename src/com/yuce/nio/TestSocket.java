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
	
	
	// ����ʽ IO 
	@Test
	public void client() throws IOException {
		// 1.��ȡͨ��
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 3388));
		
		
		FileChannel fileCh = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		
		// 2.����������
		ByteBuffer bf = ByteBuffer.allocate(1024);
		
		// 3.��ȡ�����ļ�,�����͵������� 
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
		
		// 1.����ͨ��
		ServerSocketChannel ssCh = ServerSocketChannel.open();
		
		FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		
		// 2.�󶨶˿�
		ssCh.bind(new InetSocketAddress(3388));
		
		// 3.��ȡ�ͻ������ӵ�ͨ��
		SocketChannel sCh = ssCh.accept();
		
		// 4.����������
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
