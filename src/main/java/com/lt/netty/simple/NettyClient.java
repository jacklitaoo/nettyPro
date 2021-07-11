package com.lt.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        //客户端需要一个事件循环组
        EventLoopGroup group=new NioEventLoopGroup();


        try {
            //创建一个客户端启动对象
            //客户端使用的不是ServerBootStrap而是BootStrap
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            //设置线程组
            bootstrap.group(group)
                    //设置客户端通道哦的实现类（反射）
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //加入自己的处理器
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("客户端 ok....");

            //启动客户端去连接服务器端
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 6668).sync();
            //给关闭通道进行监听
            sync.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
