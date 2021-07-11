package com.lt.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws IOException {

        //创建ServerSocketChannel -》 SockerSocket

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666，在服务器段监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){
            //没有发生事件,这里我们等待1秒，如果没有时间发生（连接事件）,返回
            if(selector.select(1000)==0){
                System.out.println("服务器没有发生时间");
                continue;
            }
            //如果返回的>0，就获取到相关的selectionkey集合
            //1.如果返回的》0，表示已经获取到关注的事件
            //selector.selectedKeys() 返回关注事件的集合
            //通过selectedKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                //根据key 对应的通道发生的事件做相应处理
                SelectionKey key = iterator.next();
                //如果是OP_ACCEPT,有新的客户端连接
                if(key.isAcceptable()){
                    //该客户端生成一个SocketChannel
                    SocketChannel socketChannel=serverSocketChannel.accept();
                    //将SocketChannel设置成非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel 注册到selector,关注事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //发生 OP_READ
                if(key.isReadable()){
                    //通过key反向获取到对应的Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该Channel关联的Buffer
                    ByteBuffer byteBuffer = (ByteBuffer)key.attachment();
                    channel.read(byteBuffer);
                    System.out.println("from 客户端" + new String(byteBuffer.array()));
                }
                iterator.remove();
            }

            }

        }
    }

