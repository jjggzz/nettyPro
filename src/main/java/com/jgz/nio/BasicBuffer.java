package com.jgz.nio;

import java.nio.IntBuffer;

/**
 * @author jgz
 * @date 2020/8/10
 * @desc buffer的使用
 **/
public class BasicBuffer {


    public static void main(String[] args) {
        //创建缓冲区
        IntBuffer intBuffer = IntBuffer.allocate(10);
        //循环写入
        for (int i = 0; i < 10; i++) {
            intBuffer.put(i);
        }
        //切换模式
        intBuffer.flip();
        //获取
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
