package com.dtstep.lighthouse.common.rpc.netty;

import com.dtstep.lighthouse.common.entity.rpc.RpcMsgType;
import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.common.rpc.netty.ProcessedFuture;
import com.dtstep.lighthouse.common.schedule.ScheduledThreadPoolBuilder;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.zeroc.IceInternal.Ex;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Map<ChannelId, InetSocketAddress> clientAddresses = new HashMap<>();

    private static final Map<InetSocketAddress,ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();

    @Override
    public synchronized void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        if (remoteAddress != null) {
            String remoteIp = remoteAddress.getAddress().getHostAddress();
            System.out.println("Client connected from IP: " + remoteIp + ",channel:" + ctx.channel().id());
            clientAddresses.put(ctx.channel().id(), remoteAddress);
        } else {
            System.err.println("Remote address is null");
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof RpcResponse){
            RpcResponse<?> response = (RpcResponse<?>) msg;
            if(response.getType() == RpcMsgType.Normal){
                String requestId = response.getRequestId();
                CompletableFuture<RpcResponse<?>> completableFuture = ProcessedFuture.remove(requestId);
                if(completableFuture != null){
                    completableFuture.complete(response);
                }
            }
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setType(RpcMsgType.HeartBeat);
                rpcRequest.setRequestId(UUID.randomUUID().toString());
                ctx.writeAndFlush(rpcRequest);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private static final ScheduledExecutorService service = ScheduledThreadPoolBuilder.
            newScheduledThreadPoolExecutor(1,new BasicThreadFactory.Builder().namingPattern("demo-consumer-schedule-pool-%d").daemon(true).build());

    @Override
    public synchronized void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = clientAddresses.remove(ctx.channel().id());
        NettyClientAdapter.setState(remoteAddress,false);
        ScheduledFuture<?> current = scheduledFutureMap.get(remoteAddress);
        if(current != null && !current.isCancelled()){
            current.cancel(true);
            scheduledFutureMap.remove(remoteAddress);
        }
        ScheduledFuture<?> scheduledFuture = service.scheduleWithFixedDelay(new RefreshThread(remoteAddress), 0,1, TimeUnit.MINUTES);
        scheduledFutureMap.put(remoteAddress,scheduledFuture);
        super.channelInactive(ctx);
    }


    private static class RefreshThread implements Runnable {

        private final InetSocketAddress inetSocketAddress;

        public RefreshThread(InetSocketAddress inetSocketAddress){
            this.inetSocketAddress = inetSocketAddress;
        }

        @Override
        public void run() {
            ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(inetSocketAddress);
            if(scheduledFuture == null){
                return;
            }
            if(NettyClientAdapter.getState(inetSocketAddress)){
                if(!scheduledFuture.isCancelled()){
                    scheduledFuture.cancel(true);
                }
            }else {
                NettyClientAdapter.connect();
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

