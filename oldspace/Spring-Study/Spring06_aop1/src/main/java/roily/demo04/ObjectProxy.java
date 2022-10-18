package roily.demo04;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ObjectProxy implements InvocationHandler {

    private Object object;

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getProxy(){
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.before(proxy.getClass().getClassLoader());
        Object invoke = method.invoke(object, args);
        this.after(proxy.getClass().getClassLoader());
        return invoke;
    }

    public void before(ClassLoader c){

        System.out.println(c.getClass().getPackage()+"方法开始执行");
    }
    public void after(ClassLoader c){

        System.out.println(c.getClass().getName()+"方法执行结束");
    }
}
