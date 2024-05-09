package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.serializer.HessianSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy {
    public static <T> T create(Class<?> clazz){

        MethodProxy proxy = new MethodProxy(clazz);
        Class<?> [] interfaces = clazz.isInterface() ?
                new Class[]{clazz} :
                clazz.getInterfaces();
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,proxy);
        return result;
    }

    public static class MethodProxy implements InvocationHandler {
        private Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(Object.class.equals(method.getDeclaringClass())){
                return method.invoke(this,args);
            }else{
                return rpcInvoke(proxy,method,args);
            }
        }

        private Object rpcInvoke(Object proxy, Method method, Object[] args) {
            com.dtstep.lighthouse.standalone.rpc.RpcRequest msg = new com.dtstep.lighthouse.standalone.rpc.RpcRequest();
            msg.setClassName(this.clazz.getName());
            msg.setMethodName(method.getName());
            msg.setParameterTypes(method.getParameterTypes());
            msg.setParameterValues(args);
            final NettyClientHandler clientHandler = new NettyClientHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap client = new Bootstrap();
                client.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                int fieldLength = 4;
                                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,fieldLength,0,fieldLength));
                                pipeline.addLast(new LengthFieldPrepender(fieldLength));
                                pipeline.addLast("encoder",new com.dtstep.lighthouse.standalone.rpc.RpcEncoder(RpcRequest.class,new HessianSerializer()));
                                pipeline.addLast("decoder",new com.dtstep.lighthouse.standalone.rpc.RpcDecoder(RpcResponse.class,new HessianSerializer()));
                                pipeline.addLast("handler",clientHandler);
                            }
                        })
                        .option(ChannelOption.TCP_NODELAY, true);
                ChannelFuture future = client.connect("localhost",8081).sync();
                future.channel().writeAndFlush(msg).sync();
                future.channel().closeFuture().sync();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }
            return clientHandler.getResponse();
        }
    }
}