package com.jgz.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可以让文件直接再内存中修改(堆外内存),应用程序不需要再拷贝一次
 * @author jgz
 * @date 2020/8/11
 * @desc 映射缓冲区
 **/
public class MappedByteBufferDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:/1.txt", "rw");
        //获取对应的文件通道
        FileChannel channel = randomAccessFile.getChannel();

        //由通道直接获取到文件映射的缓冲区
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        map.put("hello".getBytes());
        channel.close();
        randomAccessFile.close();
    }
}
