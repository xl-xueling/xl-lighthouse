package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.random.RandomID;
import io.netty.channel.Channel;
import io.netty.channel.pool.*;
import io.netty.util.concurrent.Future;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class RemoteProxy implements InvocationHandler {

    private static final InetSocketAddress address = new InetSocketAddress("localhost", 8082);

    private final Class<?> clazz;

    private final ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap;

    public RemoteProxy(Class<?> clazz, ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap) {
       this.clazz = clazz;
       this.poolMap = poolMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return invoke(method,args);
    }

    public Object invoke(Method method, Object[] args) throws Exception {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(RandomID.id(32));
        rpcRequest.setClassName(this.clazz.getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameterValues(args);
        ChannelPool pool = poolMap.get(address);
        Future<Channel> future = pool.acquire().sync();
        Channel channel = future.getNow();
        try {
            channel.writeAndFlush(rpcRequest).sync();
        } finally {
            pool.release(channel);
        }
        return null;
    }
}
