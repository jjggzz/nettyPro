package com.jgz.nio.zerocopy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author jgz
 * @date 2020/8/17
 * @desc nio零拷贝服务端
 **/
public class NioZeroCopyServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind( new InetSocketAddress(6666));

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        SocketChannel accept = serverSocketChannel.accept();

        FileOutputStream fileOutputStream = new FileOutputStream("D:/abc.iso");
        FileChannel channel = fileOutputStream.getChannel();
        int len = 0;
        int pos = 0;
        while ((len = accept.read(byteBuffer)) > -1){
            byteBuffer.flip();
            channel.write(byteBuffer,pos);
            byteBuffer.clear();
            pos += len;
        }


    }
}
