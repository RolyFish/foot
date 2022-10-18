package com.lin.demo01;

//客户
public class Client {
    public static void main(String[] args) {
        //房东要出租房子
        Host host = new Host();
        //代理，中介帮房东，但是，代理角色一般会有一些附属操作
        Proxy proxy = new Proxy(host);
        //你不用面对房东，直接找中介即可
        proxy.rent();
    }
}
