package com.jgz.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author jgz
 * @date 2020/8/10
 * @desc 文件通道示例
 **/
public class NioFileChannel {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        fileCopyByTrans("C:\\Users\\jgz\\Desktop\\Netty核心技术及源码剖析.pdf","D:/netty.pdf");
        System.out.println(System.currentTimeMillis() - start);
    }

    public static void fileCopyByTrans(String src,String des)throws IOException {
        //获取文件流对象
        FileInputStream fileInputStream = new FileInputStream(src);
        FileOutputStream fileOutputStream = new FileOutputStream(des);
        //获取文件通道
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
        //文件拷贝(大文件可分段拷贝)
//        fileOutputStreamChannel.transferFrom(fileInputStreamChannel,0,fileInputStreamChannel.size()/4);
//        fileOutputStreamChannel.transferFrom(fileInputStreamChannel,fileInputStreamChannel.size()/4,fileInputStreamChannel.size()/2);
//        fileOutputStreamChannel.transferFrom(fileInputStreamChannel,fileInputStreamChannel.size()/2,fileInputStreamChannel.size()/4*3);
//        fileOutputStreamChannel.transferFrom(fileInputStreamChannel,fileInputStreamChannel.size()/4*3,fileInputStreamChannel.size());

        fileOutputStreamChannel.transferFrom(fileInputStreamChannel,0,fileInputStreamChannel.size());
        //关闭资源
        fileOutputStreamChannel.close();
        fileInputStreamChannel.close();
        fileOutputStream.close();
        fileInputStream.close();
    }

    public static void fileCopy(String src,String des)throws IOException {
        //获取文件流对象
        FileInputStream fileInputStream = new FileInputStream(src);
        FileOutputStream fileOutputStream = new FileOutputStream(des);
        //获取文件通道
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
        //文件拷贝
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (fileInputStreamChannel.read(byteBuffer) > -1){
            byteBuffer.flip();
            fileOutputStreamChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        //关闭资源
        fileOutputStreamChannel.close();
        fileInputStreamChannel.close();
        fileOutputStream.close();
        fileInputStream.close();
    }

    public static void readFile() throws IOException {
        //创建文件输入流
        FileInputStream fileInputStream = new FileInputStream("D:/1.txt");
        //获取通道
        FileChannel channel = fileInputStream.getChannel();
        //读取内容
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len = 0;
        while ((len = channel.read(byteBuffer)) > -1){
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, len, StandardCharsets.UTF_8));
            byteBuffer.clear();
        }
        //关闭资源
        channel.close();
        fileInputStream.close();
    }

    public static void writeFile() throws IOException{
        String str = "hello 世界";
        //创建文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream("D:/1.txt");
        //获取通道
        FileChannel channel = fileOutputStream.getChannel();
        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //写入内容 编码格式utf-8
        byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        channel.write(byteBuffer);
        //关闭资源
        byteBuffer.clear();
        channel.close();
        fileOutputStream.close();
    }
}
