package com.yuce.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.xml.stream.events.StartDocument;

import org.junit.jupiter.api.Test;

class TestChannel {
	
	@Test
	public void testEncoder() throws CharacterCodingException {
		
		Charset cs1 = Charset.forName("GBK");
		// ��ȡ������
		CharsetEncoder charEncoder = cs1.newEncoder();
		
		// ��ȡ������
		CharsetDecoder charDecoder = cs1.newDecoder();
		
		// ����������
		CharBuffer cBuf = CharBuffer.allocate(1024);
		cBuf.put("�����");
		cBuf.flip();
		
		// ����
		ByteBuffer bBuf = charEncoder.encode(cBuf);
		
		for(int i = 0; i < bBuf.limit(); i++) {
			System.out.println(bBuf.get());
		}
		
		// ����
		
		bBuf.flip();
		CharBuffer cBuf2 = charDecoder.decode(bBuf);
		System.out.println(cBuf2.toString());
		
		
		
	}
	
	@Test
	public void testGather() throws IOException {
		
		RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");
		
		// 1. ��ȡͨ��
		FileChannel channel1 = raf1.getChannel();
		
		// 2. ����ָ����С�Ļ�����
		ByteBuffer buf1 = ByteBuffer.allocate(100);
		ByteBuffer buf2 = ByteBuffer.allocate(1024);
		
		// 3. ��ɢ��ȡ
		ByteBuffer[] bufs = {buf1, buf2};
		channel1.read(bufs);
		
		for(ByteBuffer byteBuffer: bufs) {
			byteBuffer.flip();
		}
		
		System.out.println(new String(bufs[0].array(), 0,bufs[0].limit()));
		System.out.println("-----------------------");
		
		System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));
		
		// 4. �ۼ�д��
		RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
		FileChannel channel2 = raf2.getChannel();
		
		channel2.write(bufs);
	}
	
	// ͨ��֮������ݴ���, ���ٽ��� ������
	@Test
	public void testNoUseBuffer() throws IOException {
		
		FileChannel inChannel = FileChannel.open(Paths.get(""), StandardOpenOption.READ);
		
		FileChannel outChannel = FileChannel.open(Paths.get(""), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		
		inChannel.transferTo(0, inChannel.size(), outChannel);
		
		inChannel.close();
		outChannel.close();
	}
	
	
	// ʹ��ֱ�ӻ���������ļ��ĸ���(�ڴ�ӳ���ļ�)
	@Test
	public void testDirect() throws IOException {
		
		long begin = System.currentTimeMillis();
		
		FileChannel inChannel = FileChannel.open(Paths.get("E:/Ѹ������/�˲������ɪ������.The Marvelous Mrs. Maisel.1080p.Orange��Ļ��/�˲������ɪ������.The.Marvelous.Mrs. Maisel.S01E01. 1080P.Orange��Ļ��.mp4"), StandardOpenOption.READ);
		
		// StandardOpenOption.CREATE_NEW(û�и��ļ�,�򴴽�;���и��ļ�,�򱨴�)
		// StandardOpenOption.CREATE (û�и��ļ�,�򴴽�;���и��ļ�,�򸲸�)
		FileChannel outChannel = FileChannel.open(Paths.get("G:/2.mkv"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		
		// �ڴ�ӳ���ļ�
		MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
		// ֱ�Ӷѻ������������ݵĶ�д����
		byte[] dst = new byte[inMappedBuf.limit()];
		inMappedBuf.get(dst);
		outMappedBuf.put(dst);
		
		long end = System.currentTimeMillis();
		System.out.println("����:" + ( end- begin));
		outChannel.close();
		inChannel.close();
		
		
	}
	
	// ʹ�÷�ֱ�ӻ�����
	@Test
	void test() {
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		// 1.��ȡͨ��
		
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		
		try {
			fis = new FileInputStream("1.jpg");
			fos = new FileOutputStream("2.jpg");
			inChannel = fis.getChannel();
			outChannel = fos.getChannel();
			// 2.�½�������
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			// 3.ͨ���е����ݶ�����������
			while (inChannel.read(buffer) != -1) {
				// �л���ȡģʽ
				buffer.flip();

				// 4.������д��ͨ����
				outChannel.write(buffer);
				buffer.clear();
			} 
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			
			if(outChannel != null) {
				try {
						outChannel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(inChannel != null) {
				
				try {
					inChannel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fos != null) {
				
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

}
