import com.roily.pojo.People;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test {
    @Test
    public void test01(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        People pople = (People) context.getBean("people");
        pople.getCat().shut();
        pople.getDog().shut();
    }
}
