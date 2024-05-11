package com.dtstep.lighthouse.common.rpc.netty;

import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import com.dtstep.lighthouse.common.util.IpUtils;
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class NettyClientAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientAdapter.class);

    private static final List<InetSocketAddress> addressList = new ArrayList<>();

    private final Bootstrap bootstrap;

    private final ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap;

    private static final int maxConnections = 10;

    public static void init(String locators) throws Exception {
        String[] locatorArr = locators.split(",");
        for (String conf : locatorArr) {
            String[] arr = conf.split(":");
            String ip = arr[0];
            int port = Integer.parseInt(arr[1]);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip,port);
            if(!addressList.contains(inetSocketAddress) && IpUtils.checkIpPort(ip,port)){
                addressList.add(inetSocketAddress);
            }
        }
    }

    public NettyClientAdapter() {
        EventLoopGroup group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(group).channel(NioSocketChannel.class);
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
        RemoteProxy proxy = new RemoteProxy(addressList,clazz,poolMap);
        Class<?> [] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,proxy);
    }
}