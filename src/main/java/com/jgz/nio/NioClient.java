package com.jgz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author jgz
 * @date 2020/8/11
 * @desc nio网络通信客户端
 **/
public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);

        if(!socketChannel.connect(new InetSocketAddress("127.0.0.1",6666))){
            while (!socketChannel.finishConnect()){
                System.out.println("连接需要时间,可在此做其他事...");
            }
        }

        System.out.println("连接成功...");

        String str = "hello server";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(byteBuffer);
        byteBuffer.clear();
        System.in.read();
    }
}
