package com.poffingames.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.msgpack.MessagePack;

public class ServerHandler extends SimpleChannelInboundHandler<MessagePack> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePack msg) throws Exception {
        // 处理入站的 MessagePack 消息
        // ...
    }

    //channelActive 方法是 Netty 中的一个回调方法，它在连接建立时被调用。当一个新的连接被建立并且可以开始传输数据时，
    // Netty 的 ChannelHandler 会收到一个 channelActive 事件通知。
    //在服务器端，channelActive 方法通常用于执行一些初始化操作或发送欢迎消息给客户端。
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
