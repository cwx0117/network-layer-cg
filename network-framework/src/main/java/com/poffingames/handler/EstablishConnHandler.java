package com.poffingames.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EstablishConnHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 在连接建立时执行的逻辑
        System.out.println("Client connected: " + ctx.channel().remoteAddress());

        // 发送欢迎消息给客户端
        String welcomeMsg = "Welcome to the server!";
        ctx.writeAndFlush(welcomeMsg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理读取到的数据
        // ...
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
