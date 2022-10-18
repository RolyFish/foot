import com.roily.config.JavaConfig;

import com.roily.config.JavaConfig2;
import com.roily.pojo.User1;
import com.roily.pojo.User2;
import com.roily.pojo.User3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

    @org.junit.Test
    public void test01(){
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        User1 getUser = (User1) context.getBean("getUser1");
        System.out.println(getUser.getName());
    }
    @org.junit.Test
    public void test02(){
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        User2 getUser = (User2) context.getBean("getUser2");
        System.out.println(getUser.getName());
    }
    @org.junit.Test
    public void test03(){
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        User3 getUser = (User3) context.getBean("getUser3");
        System.out.println(getUser.getName());
    }

    @org.junit.Test
    public void test04(){
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class,JavaConfig2.class);
        User3 getUser = (User3) context.getBean("getUser3");
        System.out.println(getUser.getName());
    }

    @org.junit.Test
    public void test05(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        User2 user2 = context.getBean("getUser2", User2.class);
        System.out.println(user2.getName());
    }

    @org.junit.Test
    public void test06(){
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        Object user2 = context.getBean(User2.class);
        System.out.println(user2);
    }

    @org.junit.Test
    public void test07(){
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        User3 user = context.getBean(User3.class);
        System.out.println(user.getAddress().getAddressName());
    }
}
