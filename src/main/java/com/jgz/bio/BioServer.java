package com.jgz.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 每有一个客户端连接就新开一个线程去处理该客户端的请求
 * @author jgz
 * @date 2020/8/10
 * @desc bio的服务端
 **/
public class BioServer {

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            20,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了.....");

        while (!serverSocket.isClosed()){
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端...");
            threadPoolExecutor.execute(()-> {
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while ( (len = inputStream.read(bytes)) > -1){
                        System.out.println(new String(bytes,0,len));
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
