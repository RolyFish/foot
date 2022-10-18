package roily.demo03;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Roly_Fish
 */
public class ProxyInvh implements InvocationHandler {

    private Rent rent;

    public void setRent(Rent rent) {
        this.rent = rent;
    }

    public Rent getProxy(){
        return (Rent) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                rent.getClass().getInterfaces(),this);
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        seehouse(rent.getClass().getName());
        Object result = method.invoke(rent, args);
        return result;
    }
    public void seehouse(String msg){
        System.out.println("代理对象:看房"+msg);
    }
}
