package com.yuce.nio;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

import org.junit.jupiter.api.Test;

class TestPipe {

	@Test
	void test() throws IOException {
		// 1. ��ȡ�ܵ�
		Pipe pipe = Pipe.open();
		
		// 2. ���������е�����д��ܵ�
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		Pipe.SinkChannel sinkCh = pipe.sink();
		buf.put("ͨ������ܵ���������".getBytes());
		
		buf.flip();
		sinkCh.write(buf);
		
		// 3. ��ȡ����ܵ��е�ֵ
		
		Pipe.SourceChannel sourceCh = pipe.source();
		buf.flip();
		int len = sourceCh.read(buf);
		System.out.println(new String(buf.array(), 0, len));
		
		sourceCh.close();
		sinkCh.close();
		
	}

}
