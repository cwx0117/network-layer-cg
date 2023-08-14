package com.poffingames.server;

import com.poffingames.handler.MessagePackDecoder;
import com.poffingames.handler.MessagePackEncoder;
import com.poffingames.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TcpServerHelper {

    private static final Logger logger = LogManager.getLogger(TcpServerHelper.class);
    public void start(String serverName) {
        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;
        ServerBootstrap bootstrap = new ServerBootstrap();

        if (Epoll.isAvailable()) {
            bossGroup = new EpollEventLoopGroup();
            workerGroup = new EpollEventLoopGroup();
            bootstrap.channel(EpollServerSocketChannel.class);
            logger.info(serverName + " epoll init");
        } else {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            bootstrap.channel(NioServerSocketChannel.class);
            logger.info(serverName + " nio init");
        }

        bootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initPipeline(ch.pipeline());
                    }
                });

        // ... 进行其他配置或启动操作
    }


    private void initPipeline(ChannelPipeline pipeline) {
        // 在这里初始化 ChannelPipeline，添加各种处理器等
        // 添加 LengthFieldBasedFrameDecoder，用于处理拆包和粘包问题
        // 参数说明：
        // - maxFrameLength：单个帧的最大长度
        // - lengthFieldOffset：长度字段的偏移量
        // - lengthFieldLength：长度字段的长度
        // - lengthAdjustment：长度调整值，用于修正长度字段的值，例如加上长度字段自身的长度
        // - initialBytesToStrip：从帧中跳过的字节数，一般为长度字段的长度
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                1024, 0, 4, 0, 4));
        pipeline.addLast("messagePackDecoder", new MessagePackDecoder());
        pipeline.addLast("serverHandler", new ServerHandler());
        // 添加 LengthFieldPrepender，用于添加长度字段
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("messagePackEncoder", new MessagePackEncoder());

    }

}
