import com.lin.pojo.User;
import com.lin.pojo.UserT;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = (User) context.getBean("user");
        //别名
        //User user = (User) context.getBean("aaaaaa");
        UserT user2=(UserT)context.getBean("ccc");

        user.show();
        user2.show();
    }
}
