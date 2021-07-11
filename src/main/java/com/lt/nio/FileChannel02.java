package com.lt.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannel02 {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel01 = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel channel = fileOutputStream.getChannel();

        while (true){

            //这里有一个重要的操作，一定不要忘了
            byteBuffer.clear();
            int read = channel.read(byteBuffer);
            if(read==-1){
                break;
            }
            byteBuffer.flip();
            channel.write(byteBuffer);
        }

        //关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
