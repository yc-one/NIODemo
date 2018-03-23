package com.yuce.nio;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

class TestBuffer {

	@Test
	void test() {
		
		String str = "abcde";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		System.out.println("------------------------");
		
		// д��
		buffer.put(str.getBytes());
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		System.out.println("------------------------");
		
		// �л���ȡģʽ
		buffer.flip();
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		System.out.println("------------------------");

		// ��ȡ
		byte[] src = new byte[buffer.limit()];
		buffer.get(src);
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		System.out.println("src:" + new String(src, 0, src.length));
		System.out.println("------------------------");
		
		// rewind �ظ���
		buffer.rewind();
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		System.out.println("------------------------");
		
		// clear ��ջ�����, ���ǻ������л�������,ֻ�Ǵ��ڡ���������״̬
		buffer.clear();
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		System.out.println("clear() ��,buffer �л�������:" + (char)buffer.get());
		System.out.println("------------------------");
		
	}
	
	@Test
	public void mark() {
		
		String str = "abcde";
		// 1. 新建一个缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		// 2. 写入缓冲区
		buf.put(str.getBytes());
		
		System.out.println(buf.limit()); // 5
		
		buf.flip();
		// 3. 读2个字节到 by 字节数组中
		byte[] bt = new byte[buf.limit()];
		buf.get(bt, 0, 2);
		
		System.out.println(new String(bt, 0, bt.length));
		buf.mark();
		
		buf.get(bt, 2, 3);
		System.out.println(new String(bt, 0, bt.length));
		
		buf.reset();
		System.out.println(buf.position());
		
		
		
		
	}
	
	@Test
	public void testMark() {
		String str="abcde";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(str.getBytes());
		
		buffer.flip();
		
		byte[] dst = new byte[buffer.limit()];
		
		// ������������, get(byte[], int offset, int length)�Ѷ��������ݴ� dst[offset] ��ʼ��,��lengeth���ַ�
		buffer.get(dst, 0, 2);
		System.out.println(new String(dst, 0, dst.length));
		
		// mark ���
		buffer.mark();
		// position Ϊ 2
		
		buffer.get(dst, 2, 3);
		// ��ʱ position Ϊ 5
		System.out.println(new String(dst, 2, 3));
		
		
		// reset �ָ� position �� mark λ��
		buffer.reset();
		System.out.println(buffer.position());
	}

}
