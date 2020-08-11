package com.jgz.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 分散读取:将一个通道中的内容按顺序分散读取到多个缓冲区中
 * 聚集写入:将多个缓冲区的内容按顺序一起写入到一个通道中
 * @author jgz
 * @date 2020/8/11
 * @desc 分散读取与聚集写入
 **/
public class ScatteringAndGathering {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile1 = new RandomAccessFile("C:\\Users\\jgz\\Desktop\\shell\\kafka\\kafka_install.sh", "rw");
        RandomAccessFile randomAccessFile2 = new RandomAccessFile("C:\\Users\\jgz\\Desktop\\shell\\kafka\\out.sh", "rw");
        FileChannel inChannel = randomAccessFile1.getChannel();
        FileChannel outChannel = randomAccessFile2.getChannel();
        //缓冲区数组
        ByteBuffer[] byteBuffers = {ByteBuffer.allocate(1024),ByteBuffer.allocate(1024)};
        //分散读取到缓冲区数组
        inChannel.read(byteBuffers);
        //切换到读模式
        for (ByteBuffer byteBuffer : byteBuffers) {
            byteBuffer.flip();
        }
        //聚集写入到outChannel中
        outChannel.write(byteBuffers);
    }

}
