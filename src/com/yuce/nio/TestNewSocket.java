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
	
	// ������ʽ IO
	@Test
	public void client() throws IOException {
		
		// 1. ����ͨ��
		SocketChannel sCh = SocketChannel.open(new InetSocketAddress("127.0.0.1", 1100));
		
		// 2. �л�Ϊ������ģʽ
		sCh.configureBlocking(false);
		
		// 3. ����ָ����С�Ļ�����
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		// 4. �������ݸ��ͻ���
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
		
		
		
		// 1.��ȡѡ����
		Selector selector = Selector.open();
		// 2.ע��ѡ����,ָ�����״̬
		ssCh.register(selector, SelectionKey.OP_ACCEPT);
		
		// 3.��ѯʽ�Ļ�ȡѡ�������Ѿ�׼���������¼�
		while(selector.select() > 0) {
			
			// 4.��ȡ��ǰѡ����������ע���"SelectionKey(�Ѿ����ļ����¼�)"
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				// 5. ��ȡ׼��"����"�����¼�
				SelectionKey sk = iterator.next();
				
				// 6. �ж��¼�����
				if(sk.isAcceptable()) {
					
					// 7. �������ܾ�����,��ȡ�ͻ�������
					SocketChannel sCh = ssCh.accept();
					
					// 8. �л�������ģʽ
					sCh.configureBlocking(false);
					
					// 9. ����ͨ��ע�ᵽѡ������
					sCh.register(selector, SelectionKey.OP_READ);
					
				} 
				else if(sk.isReadable()) {
					// 10. ��ȡ��ǰѡ�����С���������״̬��ͨ��
					SocketChannel sChannel = (SocketChannel) sk.channel();
					
					// 11. ��ȡ����
					ByteBuffer buf = ByteBuffer.allocate(1024);
					
					int len = 0;
					while((len = sChannel.read(buf)) != -1) {
						buf.flip();
						System.out.println(new String(buf.array(), 0, len));
						buf.clear();
					}
					
				}
				
				// 12. ȡ��ѡ���, SelectionKey
				iterator.remove();
			}
		}
		
	}
	
}
