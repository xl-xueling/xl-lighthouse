package com.dtstep.lighthouse.core.http;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.util.IpUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightHouseHttpService {

    private static final Logger logger = LoggerFactory.getLogger(LightHouseHttpService.class);

    public static void start() {
        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;

        if (Epoll.isAvailable()) {
            bossGroup = new EpollEventLoopGroup();
            workerGroup = new EpollEventLoopGroup();
        } else if (KQueue.isAvailable()) {
            bossGroup = new KQueueEventLoopGroup();
            workerGroup = new KQueueEventLoopGroup();
        } else {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
        }

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class :
                            (KQueue.isAvailable() ? KQueueServerSocketChannel.class : NioServerSocketChannel.class))
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new HttpObjectAggregator(50 * 1024 * 1024))
                                    .addLast(new HttpServiceHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 10 * 1024 * 1024)
                    .childOption(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024);
            ChannelFuture f = bootstrap.bind(SysConst.CLUSTER_MONITOR_SERVICE_PORT).sync();
            f.channel().closeFuture().sync();
        } catch (Exception ex) {
            logger.error("ldp standalone http service startup exception!", ex);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
