package com.lt.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyByteBuf02 {

    public static void main(String[] args) {
        //创建buffer
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", Charset.forName("UTF-8"));

        //使用相关的方法 true
        if(byteBuf.hasArray()){
            byte[] array = byteBuf.array();

            //将content转成字符串

            System.out.println(new String(array, Charset.forName("UTF-8")));

            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            //可读的字节数 12
            int i = byteBuf.readableBytes();
            System.out.println("len="+i);

            //使用for取出各个字节
            for (int j = 0; j < i; j++) {
                System.out.println((char)byteBuf.getByte(i));
            }

            //按照某个范围读取
            System.out.println(byteBuf.getCharSequence(0,4,Charset.forName("UTF-8")));
            System.out.println(byteBuf.getCharSequence(4,5,Charset.forName("UTF-8")));
        }
    }
}
