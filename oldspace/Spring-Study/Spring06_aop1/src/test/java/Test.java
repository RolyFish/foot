import roily.demo03.Host;
import roily.demo03.ProxyInvocationHandler;
import roily.demo03.Rent;

public class Test {

    @org.junit.Test
    public void test01(){

        Host host = new Host();
        ProxyInvocationHandler proxyInvocationHandler= new ProxyInvocationHandler();
        proxyInvocationHandler.setTarget(host);
        Rent proxy = (Rent) proxyInvocationHandler.getProxy();
        proxy.rent();
    }

}
