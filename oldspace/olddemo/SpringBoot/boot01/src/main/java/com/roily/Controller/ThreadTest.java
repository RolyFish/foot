package com.roily.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/3/1
 */
@RestController
public class ThreadTest {

    Integer num = 100;

    @RequestMapping("/thread")
    public void threadTest() {

        //synchronized (num) {
            System.out.println("线程" + Thread.currentThread().getName() + "开始");
            num--;
            System.out.println(num);
            System.out.println("线程" + Thread.currentThread().getName() + "结束");
        //}


    }

}
