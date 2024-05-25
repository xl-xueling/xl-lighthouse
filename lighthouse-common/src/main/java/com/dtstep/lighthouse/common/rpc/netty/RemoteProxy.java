package com.dtstep.lighthouse.common.rpc.netty;

import com.dtstep.lighthouse.common.entity.rpc.RpcMsgType;
import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.StringUtil;
import io.netty.channel.Channel;
import io.netty.channel.pool.*;
import io.netty.util.concurrent.Future;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class RemoteProxy implements InvocationHandler {

    private final Class<?> clazz;

    private final List<InetSocketAddress> addressList;

    public RemoteProxy(List<InetSocketAddress> addressList,Class<?> clazz) {
       this.addressList = addressList;
       this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return invoke(method,args);
    }

    public Object invoke(Method method, Object[] args) throws Exception {
        RpcRequest rpcRequest = new RpcRequest();
        String reqId = RandomID.id(32);
        rpcRequest.setRequestId(reqId);
        rpcRequest.setClassName(this.clazz.getName());
        rpcRequest.setMethodName(method.getName());
        CompletableFuture<RpcResponse<?>> completableFuture = new CompletableFuture<>();
        ProcessedFuture.put(reqId,completableFuture);
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameterValues(args);
        rpcRequest.setType(RpcMsgType.Normal);
        ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap = NettyClientAdapter.getPoolMap();
        ChannelPool pool = poolMap.get(addressList.get(ThreadLocalRandom.current().nextInt(addressList.size())));
        System.out.println("poolMap id:" + poolMap + ",pool id:" + pool);
        Future<Channel> future = pool.acquire().sync();
        Channel channel = future.getNow();
        try {
            channel.writeAndFlush(rpcRequest).sync();
        } finally {
            pool.release(channel);
        }
        RpcResponse<?> res = completableFuture.get();
        if(StringUtil.isNotEmpty(res.getError())){
            throw new LightRpcException(res.getError());
        }
        return res.getResult();
    }
}
