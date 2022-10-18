package com.lin.demo03;

import com.lin.demo03.Rent;

//房东
public class Host implements Rent{

    public void rent() {
        System.out.println("房东要出租房子");
    }
}
