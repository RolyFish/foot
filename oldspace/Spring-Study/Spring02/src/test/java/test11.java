import com.roily.pojo.Student;
import com.roily.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test11 {
    @Test
    public  void test01(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student1 = (Student) context.getBean("student");
        Student student2 = (Student) context.getBean("student");
        System.out.println(student1==student2);
        System.out.println(student1.getName());
        student1.setName("yyc");
        System.out.println(student2.getName());
    }
    @Test
    public  void test02(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext2.xml");
        User bean1 = (User) context.getBean("user2");
        User bean2 = (User) context.getBean("user2");
        System.out.println(bean1==bean2);
    }
    @Test
    public  void test03(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext2.xml");
        User bean = (User) context.getBean("user2");
        System.out.println(bean.toString());
    }
    @Test
    public  void test04(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext2.xml");
        //User bean = (User) context.getBean("user2");
        //System.out.println(bean.toString());
    }
}
