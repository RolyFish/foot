package roily.demo01;

/**
 * 静态代理
 * @author Roly_Fish
 */
public class Proxy implements Rent{
    //将真实对象组合进来，调用他的方法，并对方法进行增强
    //private Host host = new Host();

    private Rent rent;

    public Proxy() {
    }

    //将真实对象set进来
    public void setRent(Rent rent) {
        this.rent = rent;
    }

    public void rent() {
        this.seeHouse();
        rent.rent();
    }
    public void seeHouse() {
        System.out.println("代理对象：中介：看房");
    }

}
