package com.yuce.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.Test;

public class TestNewSocket {
	
	// 非阻塞式 IO
	@Test
	public void client() throws IOException {
		
		// 1. 创建通道
		SocketChannel sCh = SocketChannel.open(new InetSocketAddress("127.0.0.1", 1100));
		
		// 2. 切换为非阻塞模式
		sCh.configureBlocking(false);
		
		// 3. 分配指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		// 4. 发送数据给客户端
		buf.put(LocalDateTime.now().toString().getBytes());
		
		buf.flip();
		
		sCh.write(buf);
		buf.clear();
		
		sCh.close();
		
	}
	
	@Test
	public void server() throws IOException {
		
		ServerSocketChannel ssCh = ServerSocketChannel.open();
		ssCh.configureBlocking(false);
		
		ssCh.bind(new InetSocketAddress(1100));
		
		
		
		// 1.获取选择器
		Selector selector = Selector.open();
		// 2.注册选择器,指定监控状态
		ssCh.register(selector, SelectionKey.OP_ACCEPT);
		
		// 3.轮询式的获取选折起上已经准备就绪的事件
		while(selector.select() > 0) {
			
			// 4.获取当前选择器中所有注册的"SelectionKey(已就绪的监听事件)"
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				// 5. 获取准备"就绪"的是事件
				SelectionKey sk = iterator.next();
				
				// 6. 判断事件类型
				if(sk.isAcceptable()) {
					
					// 7. 若“接受就绪”,获取客户端连接
					SocketChannel sCh = ssCh.accept();
					
					// 8. 切换非阻塞模式
					sCh.configureBlocking(false);
					
					// 9. 将该通道注册到选择器中
					sCh.register(selector, SelectionKey.OP_READ);
					
				} 
				else if(sk.isReadable()) {
					// 10. 获取当前选择器中“读就绪”状态的通道
					SocketChannel sChannel = (SocketChannel) sk.channel();
					
					// 11. 读取数据
					ByteBuffer buf = ByteBuffer.allocate(1024);
					
					int len = 0;
					while((len = sChannel.read(buf)) != -1) {
						buf.flip();
						System.out.println(new String(buf.array(), 0, len));
						buf.clear();
					}
					
				}
				
				// 12. 取消选择键, SelectionKey
				iterator.remove();
			}
		}
		
	}
	
}
