package com.jgz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author jgz
 * @date 2020/8/11
 * @desc nio网络通信模型
 **/
public class NioServer {
    public static void main(String[] args) throws IOException {
        //创建服务端channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建Selector
        Selector selector = Selector.open();
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //注册到selector 关注事件为accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0){
            System.out.println(selector.keys());
            System.out.println("有客户端连接了...");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();

                if (next.isAcceptable()){
                    SocketChannel accept = serverSocketChannel.accept();
                    //设置成非阻塞
                    accept.configureBlocking(false);
                    //将获取到的SocketChannel 注册到Selector,并绑定一个buffer
                    accept.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                else if (next.isReadable()){
                    SocketChannel channel = (SocketChannel) next.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) next.attachment();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    System.out.println("from client info:" + new String(byteBuffer.array()));
                }
                //移除已经操作的key
                iterator.remove();
            }
        }
    }
}
