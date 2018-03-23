package com.yuce.nio;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

import org.junit.jupiter.api.Test;

class TestPipe {

	@Test
	void test() throws IOException {
		// 1. 获取管道
		Pipe pipe = Pipe.open();
		
		// 2. 将缓冲区中的数据写入管道
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		Pipe.SinkChannel sinkCh = pipe.sink();
		buf.put("通过单向管道发送数据".getBytes());
		
		buf.flip();
		sinkCh.write(buf);
		
		// 3. 读取单向管道中的值
		
		Pipe.SourceChannel sourceCh = pipe.source();
		buf.flip();
		int len = sourceCh.read(buf);
		System.out.println(new String(buf.array(), 0, len));
		
		sourceCh.close();
		sinkCh.close();
		
	}

}
