package com.googlecode.youyou;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by gsheppard on 8/06/14.
 */
public class BetterThreadLocal {
    private Proxy proxy;

    public static <T> T threadLocal(Class<T> clazz, InitialValueFactory<T> initialValueFactory) {
        InvocationHandler handler = new MyInvocationHandler(initialValueFactory);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                handler);
    }

    private static class MyInvocationHandler<T> implements InvocationHandler {
        private ThreadLocal<T> threadLocal = new ThreadLocal<>();
        private InitialValueFactory<T> initialValueFactory;

        public MyInvocationHandler(InitialValueFactory<T> initialValueFactory) {
            this.initialValueFactory = initialValueFactory;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            T object = threadLocal.get();
            if (object == null) {
                object = initialValueFactory.create();
                threadLocal.set(object);
            }
            return method.invoke(object, args);
        }
    }

    public static interface InitialValueFactory<T> {
        T create();
    }
}
