package com.lt.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FileChannel {

    public static void main(String[] args) throws IOException {

        String str="hello,lt";
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");

        //通过输出流获取对应的FileChannel
        //这个fileChannel真实类型是fileChannelImpl
        java.nio.channels.FileChannel channel = fileOutputStream.getChannel();

        //创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

        //将str放入到byteBuffer缓冲区
        byteBuffer.put(str.getBytes());
        //对byteBuffer进行flip
        byteBuffer.flip();

        //将byteBuffer数据写入到fileChannel
        channel.write(byteBuffer);
        //关闭流
        fileOutputStream.close();


    }
}
