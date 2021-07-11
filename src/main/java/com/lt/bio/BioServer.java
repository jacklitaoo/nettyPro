package com.lt.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BioServer {

    public static void main(String[] args) {

        //线程池机制

        //创建一个线程池
        //如果有客户端连接，就创建一个线程与之通讯（单独写一个方法）
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(32), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());

        //创建ServerSocket
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(6666);
            System.out.println("服务器启动了");
            while (true) {
                //监听等待客户端连接
                System.out.println("阻塞....");
                Socket socket = serverSocket.accept();
                System.out.println("连接到一个客户端");
                //创建一个线程，与之通讯（单独写一个方法）
                threadPoolExecutor.execute(()->{
                    //可以和客户端通讯
                    handler(socket);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //编写一个handler方法和客户端通讯
    public static void handler(Socket socket){

        System.out.println("线程信息 id"+Thread.currentThread().getId());
        System.out.println("线程信息 name"+Thread.currentThread().getName());

        byte[] bytes = new byte[1024];
        InputStream inputStream=null;
        try {
            inputStream = socket.getInputStream();
            while (true){
                System.out.println("线程信息 id"+Thread.currentThread().getId());
                System.out.println("线程信息 name"+Thread.currentThread().getName());
                System.out.println("阻塞....");
                int read = inputStream.read(bytes);
                if(read!=-1){
                    System.out.println(new String(bytes,0,read));//输出客户端发送的数据
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
