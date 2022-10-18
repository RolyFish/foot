import com.roily.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class hello {
    @Test
    public void test00() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        Class<?> userClass = Class.forName("com.roily.pojo.User");
        User user = (User) userClass.newInstance();
        System.out.println(user.getName());
        Method setName =
                userClass.getDeclaredMethod("setName", String.class);
        setName.invoke(user,"123");
        System.out.println(user.getName());
    }

    @Test
    public void test01(){
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        User user1 = (User) context.getBean("user");
        User user2 = (User) context.getBean("user2");
        System.out.println(user1==user2);
        //System.out.println(user1.equals(user2));
    }
    @Test
    public void test06(){
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        User user = (User) context.getBean("asdad");
        user.show();
    }
    @Test
    public void test02(){
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        User user = (User) context.getBean("user2");
        System.out.println("=============");
        user.show();
    }
    @Test
    public void test03(){
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        User user = (User) context.getBean("user3");
        user.show();
    }
    @Test
    public void test04(){
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
//        User user = (User) context.getBean("user4");
//        user.show();
    }

}
