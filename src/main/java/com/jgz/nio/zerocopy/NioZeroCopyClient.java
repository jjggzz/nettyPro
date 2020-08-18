package com.jgz.nio.zerocopy;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author jgz
 * @date 2020/8/17
 * @desc nio零拷贝客户端
 **/
public class NioZeroCopyClient {
    public static void main(String[] args) throws IOException {

        SocketChannel open = SocketChannel.open();

        open.connect(new InetSocketAddress("127.0.0.1",6666));

        RandomAccessFile accessFile = new RandomAccessFile("C:\\Users\\jgz\\Downloads\\CentOS-7-x86_64-Minimal-2003.iso", "r");
        FileChannel channel = accessFile.getChannel();
        long start = System.currentTimeMillis();
        long l = accessFile.length();
        long currentPos = 0;
        while (l > 1024*1024*8){
            channel.transferTo(currentPos,1024*1024*8,open);
            currentPos =+ 1024 * 1024 * 8;
            l -= 1024 * 1024 * 8;
        }
        channel.transferTo(currentPos,l,open);

        System.out.println(System.currentTimeMillis() - start);

    }
}
