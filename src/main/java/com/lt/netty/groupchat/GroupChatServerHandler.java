package com.lt.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {


    //使用一个HashMap管理
    public static Map<String,Object> channelMap=new HashMap<>();
    public static Map<User,Object> channelMap1=new HashMap<>();

    //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE 是全局事件执行器，是一个单例
    private static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //handlerAdded 表示连接建立，一旦连接，第一个被执行
    //将当前Channel加入到hannelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        Channel channel = ctx.channel();


        channelMap.put("id100",channel);
        channelMap1.put(new User(),channel);
        //将该客户加入聊天的信息推送给其他在线的客户
        //该方法会将channelGroup中所有的channel遍历，并发送消息
        //不需要自己遍历
        channels.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天\n"+simpleDateFormat.format(new Date()));
        channels.add(channel);
    }

    //断开连接,将xx客户离开推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("[客户端]"+channel.remoteAddress()+" 离开了\n");
    }

    //表示channel处理活动状态,提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 上线了");
        System.out.println("channelGroup size"+channels.size());
    }

    //表示channel处于不活动状态，提示xxx离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 离线了~");
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {


        //获取到当前channel
        Channel channel = ctx.channel();
        //这是我们遍历channelGroup。根据不同的请款，回送不同的消息
        channels.forEach(ch->{
            //不是当前的channel，转发消息
            if(ch!=channel){
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+" 发送了消息"+msg+"\n");
            }else {//回显自己发送的消息给自己
                ch.writeAndFlush("[自己]发送了消息"+msg+"\n");
            }
        });
    }

    //发生异常关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }
}
