package roily.demo01;

public class Client {
    public static void main(String[] args) {
        System.out.println("client:租房的人：想租房子");
        Host host = new Host();
        Proxy proxy = new Proxy();

        proxy.setRent(host);

        proxy.rent();
    }

}
