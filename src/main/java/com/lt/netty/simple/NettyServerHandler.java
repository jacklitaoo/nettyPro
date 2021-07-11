package com.lt.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author itaoo
 * 我们自定义一个Handler需要继承netty规定好的某个HandlerAdapter
 * 这是我们自动义一个Handler，才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据事件（读取客户端发送的消息）
     * 1.
     * @param ctx ChannelHandlerContext上下文对象，含有管道pipeline，通道channel，地址等
     * @param msg 客户端发送的数据 默认是Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        //这里有一个非常耗时的业务->异步执行->提交该channel对应的NIOEventLoop的taskqueue

        //解决方案1 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(()->{
            try {
                Thread.sleep(10*1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~2",CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

 /*       Thread.sleep(10*1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~2",CharsetUtil.UTF_8));*/


        //用户自定义定时任务-》该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(()->{
            try {
                Thread.sleep(5*1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~3",CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },5, TimeUnit.SECONDS);

        System.out.println("go on.....");


        /*        System.out.println("server ctx="+ctx);

        //将msg转成一个byteBuffer
        //是netty提供的
        ByteBuf byteBuf=(ByteBuf) msg;
        System.out.println("客户端发送消息是："+ byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址:"+ctx.channel().remoteAddress());*/
    }


    /**
     * 数据读取完毕
     * @param ctx ChannelHandlerContext上下文对象，含有管道pipeline，通道channel，地址等
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是write+flush
        //将数据写入到缓存，并刷新
        //一般会对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~",CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，发生异常一般需要关闭通道
     * @param ctx  ChannelHandlerContext上下文对象，含有管道pipeline，通道channel，地址等
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }
}
