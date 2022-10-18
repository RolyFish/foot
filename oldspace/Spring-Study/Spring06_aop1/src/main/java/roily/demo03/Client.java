package roily.demo03;

import roily.demo01.Proxy;

public class Client {
    public static void main(String[] args) {

        Host host = new Host();
        ProxyInvh proxyInvh = new ProxyInvh();
        proxyInvh.setRent(host);
        Rent proxy = (Rent) proxyInvh.getProxy();
        proxy.rent();
    }


}
