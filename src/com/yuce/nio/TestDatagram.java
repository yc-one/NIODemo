package com.yuce.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class TestDatagram {

	@Test
	void send() throws IOException {
		// 1. ����ͨ��
		DatagramChannel dc = DatagramChannel.open();
		
		// 2. �л�������ģʽ
		dc.configureBlocking(false);
		
		// 3. ���仺����
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		// 4.
		buf.put(LocalDateTime.now().toString().getBytes());
		
		buf.flip();
		
		// UDP �� ʹ�� send() ����
		dc.send(buf, new InetSocketAddress("127.0.0.1", 3399));
		
		dc.close();
	}
	
	@Test
	void server() throws IOException {
		
		DatagramChannel dc = DatagramChannel.open();
		
		dc.configureBlocking(false);
		
		dc.bind(new InetSocketAddress(3399));
		
		Selector sl = Selector.open();
		
		dc.register(sl, SelectionKey.OP_READ);
		
		while(sl.select() > 0) {
			
			Iterator<SelectionKey> it = sl.selectedKeys().iterator();
			
			while(it.hasNext()) {
				
				// ��ȡ SecletionKeys
				SelectionKey sKey = it.next();
				if(sKey.isReadable()) {
					
					ByteBuffer buf = ByteBuffer.allocate(1024);
					
					dc.receive(buf);
					System.out.println(new String(buf.array(), 0, buf.limit()));
					buf.clear();
				}
			
			}
			it.remove();
		}
	}

}
