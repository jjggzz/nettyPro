package com.jgz.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author jgz
 * @date 2020/8/11
 * @desc 群聊服务端
 **/
public class GroupChatServer {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private static final int PORT = 6666;

    public GroupChatServer(){
        try {
            //打开通道
            serverSocketChannel = ServerSocketChannel.open();
            //创建选择器
            selector = Selector.open();
            //设为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //绑定监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress(6666));
            //注册选择器并监听事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void listen() {

            try {
                while (selector.select() > 0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel sc = serverSocketChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector,SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress()+" 上线");
                        }
                        else if (key.isReadable()){
                            //处理读
                            read(key);
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void read(SelectionKey key){
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            channel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            if (read > -1){
                sendInfoToOtherChannel( new String(buffer.array(),0,read), channel);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                System.out.println(channel.getRemoteAddress() + " 离线");
                key.cancel();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void sendInfoToOtherChannel(String msg,SocketChannel self){
        System.out.println("服务器群发消息..");
        selector.keys().forEach(key -> {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && ! channel.equals(self)){
                SocketChannel target = (SocketChannel) channel;
                try {
                    target.write(ByteBuffer.wrap(msg.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public static void main(String[] args) {
        new GroupChatServer().listen();
    }
}
