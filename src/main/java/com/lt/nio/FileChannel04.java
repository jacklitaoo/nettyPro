package com.lt.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannel04 {

    public static void main(String[] args) throws IOException {

        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("d:\\a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\a1.jpg");

        //获取各个流对象的Channel
        FileChannel channel = fileInputStream.getChannel();
        FileChannel channel1 = fileOutputStream.getChannel();


        //使用transferFrom完成拷贝
        channel1.transferFrom(channel1,0,channel1.size());

    }
}
