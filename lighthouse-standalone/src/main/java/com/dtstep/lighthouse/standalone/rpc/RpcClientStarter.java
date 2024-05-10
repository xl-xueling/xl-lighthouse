package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import com.dtstep.lighthouse.standalone.executive.LightStandaloneEntrance;
import com.dtstep.lighthouse.standalone.rpc.provider.StandaloneRemoteService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

public class RpcClientStarter {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientStarter.class);

    private final Bootstrap bootstrap;

    private final ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap;

    private static final int maxConnections = 10;

    public RpcClientStarter() {
        EventLoopGroup group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(group)
                .channel(NioSocketChannel.class);

        CustomIdleStateHandler customIdleStateHandler = new CustomIdleStateHandler();
        NettyClientHandler clientHandler = new NettyClientHandler();

        this.poolMap = new AbstractChannelPoolMap<>() {

            @Override
            protected ChannelPool newPool(InetSocketAddress key) {

                return new FixedChannelPool(bootstrap.remoteAddress(key), new ChannelPoolHandler() {
                    @Override
                    public void channelReleased(Channel ch) throws Exception {
                        if(logger.isDebugEnabled()){
                            logger.debug("channel released,id:" + ch.id());
                        }
                    }

                    @Override
                    public void channelAcquired(Channel ch) throws Exception {
                        if(logger.isDebugEnabled()){
                            logger.debug("channel acquired,id:" + ch.id());
                        }
                    }

                    @Override
                    public void channelCreated(Channel ch) throws Exception {
                        if(logger.isDebugEnabled()){
                            logger.debug("channel created,id:" + ch.id());
                        }
                        int fieldLength = 4;
                        ch.pipeline()
                                .addLast(customIdleStateHandler)
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, fieldLength, 0, fieldLength))
                                .addLast(new LengthFieldPrepender(fieldLength))
                                .addLast("encoder", new RpcEncoder(RpcRequest.class, new KryoSerializer()))
                                .addLast("decoder", new RpcDecoder(RpcResponse.class, new KryoSerializer()))
                                .addLast(clientHandler);
                    }
                }, maxConnections);
            }
        };
    }

    public <T> T create(Class<?> clazz){
        RemoteProxy proxy = new RemoteProxy(clazz,poolMap);
        Class<?> [] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,proxy);
    }

//    public static void main(String[] args) throws Exception {
//        RpcClientStarter client = new RpcClientStarter();
//        StandaloneRemoteService rpc = client.create(StandaloneRemoteService.class);
//        for(int i = 0;i<100;i++){
//            rpc.queryGroupInfo("Gjd:feed_behavior_stat");
//            Thread.sleep(10);
//        }
//    }
}