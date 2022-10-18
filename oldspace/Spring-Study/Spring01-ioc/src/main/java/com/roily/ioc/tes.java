package com.roily.ioc;

import com.roily.ioc.dao.impl.UserDaoImpl1;
import com.roily.ioc.dao.impl.UserDaoImpl2;
import com.roily.ioc.service.UserService;
import com.roily.ioc.service.UserService2;
import com.roily.ioc.service.impl.UserServiceImpl;
import com.roily.ioc.service.impl.UserServiceImpl2;
import org.junit.Test;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2021/12/12
 */
public class tes {
    @Test
    public void  t1(){
        UserService userService = new UserServiceImpl();
        userService.getUser1();
    }
    @Test
    public void  t2(){
        UserService userService = new UserServiceImpl();
        userService.getUser2();
    }
    @Test
    public void  t3(){
        UserServiceImpl2 userServiceImpl2 = new UserServiceImpl2();
        userServiceImpl2.setUserDao(new UserDaoImpl2());
        userServiceImpl2.getUser();
    }
    @Test
    public void  t4(){
        UserServiceImpl2 userServiceImpl2 = new UserServiceImpl2(new UserDaoImpl2());
        //userServiceImpl2.setUserDao(new UserDaoImpl2());
        userServiceImpl2.getUser();
    }

}
