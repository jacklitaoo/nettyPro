package com.lt.netty.http;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    //channelRead0 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断msg是不是httpRequest请求
         if(msg instanceof HttpRequest){


             System.out.println("对应的channel="+ctx.channel()+" pipeline="+ctx.pipeline());
             System.out.println("msg 类型="+msg.getClass());
             System.out.println( "客户端地址"+ctx.channel().remoteAddress());

             //回复信息给浏览器
             ByteBuf byteBuf = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);

             //构造一个http的响应，即httpresponse
             FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

             response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
             response.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());

             //将构建好的response
             ctx.writeAndFlush(response);
         }

    }
}
