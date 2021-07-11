package com.lt.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannel01 {

    public static void main(String[] args) throws IOException {

        //创建文件的输入流
        FileInputStream fileInputStream = new FileInputStream("D:\\file01.txt");
        //获取到channel通道,
        FileChannel channel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(fileInputStream.available());
        //将channel的数据读取到buffer中
        channel.read(allocate);

        //将缓冲区的直接数组转成String
        System.out.println(new String(allocate.array()));
        fileInputStream.close();
    }

   }
