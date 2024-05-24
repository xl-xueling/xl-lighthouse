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
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.ScheduledFuture;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static ScheduledFuture<?> scheduledFuture;

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
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyClientAdapter.setState(false);
        service.scheduleWithFixedDelay(new RefreshThread(), 0,30, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }



    private static class RefreshThread implements Runnable {
        @Override
        public void run() {
            if(NettyClientAdapter.getState()){
                service.shutdown();
            }else{
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

