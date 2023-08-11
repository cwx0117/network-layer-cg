package com.poffingames.server;

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
    }

}
