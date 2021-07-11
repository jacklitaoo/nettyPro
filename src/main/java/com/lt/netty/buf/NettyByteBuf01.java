package com.lt.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class NettyByteBuf01 {

    public static void main(String[] args) {

        //创建一个ByteBuf
        //说明
        //1.创建对象，该对象包含一个数组arr,是一个byte[10]
        //2.在netty的buffer中，不需要使用flip进行反转
        // 底层维护了readerindex和writeindex和capacity ,将buffer分成三个区域
        // 0--->readerIndex 已经读取的区域
        // readerIndex--->writerIndex 可读的区域
        // writerIndex--->capacity 表示可以的区域
        ByteBuf buffer = Unpooled.buffer(10);

        for(int i=0;i<10;i++){
            buffer.writeByte(i);

        }

        System.out.println("capacity="+buffer.capacity());
        //输出
/*        for(int i=0;i<buffer.capacity();i++){
            System.out.println(buffer.getByte(i));
        }*/

        for(int i=0;i<buffer.capacity();i++){
            System.out.println(buffer.readByte());
        }
        System.out.println("执行完毕");

    }
}
