package com.dtstep.lighthouse.common.rpc.netty;

import com.dtstep.lighthouse.common.entity.rpc.RpcMsgType;
import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.common.schedule.ScheduledThreadPoolBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    private static final Map<ChannelId, InetSocketAddress> clientAddresses = new HashMap<>();

    @Override
    public synchronized void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        if (remoteAddress != null) {
            String remoteIp = remoteAddress.getAddress().getHostAddress();
            logger.info("Client connected from IP: " + remoteIp + ",channel:" + ctx.channel().id() + ",clientAddresses size:" + clientAddresses.size());
            clientAddresses.put(ctx.channel().id(), remoteAddress);
        } else {
            logger.info("Remote address is null");
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

    @Override
    public synchronized void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = clientAddresses.remove(ctx.channel().id());
        if(remoteAddress != null){
            ctx.channel().eventLoop().schedule(new RefreshThread(remoteAddress),3,TimeUnit.MINUTES);
        }
        super.channelInactive(ctx);
    }

    private static class RefreshThread implements Runnable {

        private final InetSocketAddress inetSocketAddress;

        public RefreshThread(InetSocketAddress inetSocketAddress){
            this.inetSocketAddress = inetSocketAddress;
        }

        @Override
        public void run() {
            NettyClientAdapter.instance().reconnect(inetSocketAddress);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

