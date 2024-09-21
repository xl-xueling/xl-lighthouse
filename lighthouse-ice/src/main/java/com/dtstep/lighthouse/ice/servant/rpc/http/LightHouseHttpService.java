package com.dtstep.lighthouse.ice.servant.rpc.http;

import com.dtstep.lighthouse.core.config.LDPConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
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

    private void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new HttpObjectAggregator(1024 * 128))
                                    .addLast(new ServerHandler());
                        }
                    });
            ChannelFuture future = server.bind(18101).sync();
            future.channel().closeFuture().sync();
        }catch (Exception ex){
            logger.error("ldp standalone service startup exception!",ex);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new LightHouseHttpService().start();
    }
}
