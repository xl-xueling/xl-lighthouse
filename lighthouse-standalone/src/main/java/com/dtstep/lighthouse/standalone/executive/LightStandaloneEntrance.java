package com.dtstep.lighthouse.standalone.executive;

import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.standalone.rpc.NettyServerHandler;
import com.dtstep.lighthouse.standalone.rpc.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightStandaloneEntrance {

    private static final Logger logger = LoggerFactory.getLogger(LightStandaloneEntrance.class);

    private final int port;

    public LightStandaloneEntrance(int port) {
        this.port = port;
    }

    private void start() throws Exception {
        LDPConfig.loadConfiguration();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        NettyServerHandler.register();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());
            ChannelFuture future = server.bind(this.port).sync();
            logger.info("ldp standalone service start,listen:{}",port);
            future.channel().closeFuture().sync();
        }catch (Exception ex){
            logger.error("ldp standalone service startup exception!",ex);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new LightStandaloneEntrance(4061).start();
    }
}
