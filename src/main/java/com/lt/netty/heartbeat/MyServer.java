package com.lt.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {

    public static void main(String[] args) throws InterruptedException {


        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //在BossGroup增加日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty提供IdleStateHandler
                            /**
                             * 说明
                             * 1.IdleStateHandler是netty提供的处理空闲状态的处理器
                             * 2. 参数1 readerldleTime :表示服务器多长时间没有从管道读取数据，就会发送一个心跳检测包检测是否连接
                             * 3. 参数2 writerldleTime :表示服务器多长时间没有把数据写入管道，就会发送一个心跳检测包检测是否连接
                             * 4. 参数3 allIdleTime : 表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                             * 当IdleStateEvent触发后，就会传递给管道的下一个handler去处理
                             * 通过调用（触发）下一个handler的userEventTiggered ，在该方法中去处理当IdleStateEvent触发后的事件
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));

                            //加入一个对空闲检测进一步处理的handler（自动义）
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            //启动服务器
            ChannelFuture bind = serverBootstrap.bind(7000);
            ChannelFuture sync = bind.channel().closeFuture().sync();

        }finally {

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
