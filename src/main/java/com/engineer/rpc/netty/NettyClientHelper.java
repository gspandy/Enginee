package com.engineer.rpc.netty;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.engineer.rpc.RPCClient;

public class NettyClientHelper {
	
	public static <T> T getProxy(final RPCClient client, final Class<T> clazz) {

		InvocationHandler handler = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				NettyInvocation invo = new NettyInvocation();
				invo.setInterfaces(clazz);
				invo.setMethod(method.getName());
				invo.setMethodParameterTypes(method.getParameterTypes());
				invo.setParams(args);
				
				NettyInvocation result = client.send(invo);
				
				//future.addListener(ChannelFutureListener.CLOSE);
				return result.getResult();
			}
		};
		
		@SuppressWarnings("unchecked")
		T t = (T) Proxy.newProxyInstance(NettyClientHelper.class.getClassLoader(), new Class<?>[] { clazz }, handler);
		return t;
	}
	
}
