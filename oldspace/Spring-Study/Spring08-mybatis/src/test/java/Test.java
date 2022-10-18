import com.roily.Util.mybatisUtil;
import com.roily.dao.UserMapper;
import com.roily.dao.UserMapperImpl2;
import com.roily.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.List;

public class Test {


    @org.junit.Test
    public void test(){
        System.out.println(1);
    }

    @org.junit.Test
    public void test00(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SqlSessionTemplate sqlSession = context.getBean("sqlSession",SqlSessionTemplate.class);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();
        for (User user : userList) {
            System.out.println(user);
        }
    }
    @org.junit.Test
    public void test000(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = context.getBean("sqlSessionFactory", SqlSessionFactory.class);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();
        for (User user : userList) {
            System.out.println(user);
        }
    }
    @org.junit.Test
    public void test01(){

        SqlSession sqlSession = mybatisUtil.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();
        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }

    @org.junit.Test
    public void test02(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        List<User> userList = userMapper.getUserList();
        for (User user : userList) {
            System.out.println(user);
        }
    }
    @org.junit.Test
    public void test03(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper2");
        List<User> userList = userMapper.getUserList();
        for (User user : userList) {
            System.out.println(user);
        }
    }
    @org.junit.Test
    public void test04(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper2");
        List<User> userList = userMapper.getUserList();
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @org.junit.Test
    public void test95(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserMapper userMapperImpl = (UserMapper) context.getBean("userMapper2");

        User user = new User("于延闯", "111");
        int i = userMapperImpl.addUser(user);
        System.out.println(i+"======================");
        int j = userMapperImpl.delUser(2);
        System.out.println(j+"================");

    }

    @org.junit.Test
    public void test951(){
        ApplicationContext context
                = new ClassPathXmlApplicationContext("applicationContext.xml");
        User test = context.getBean("test",User.class);
        System.out.println(test);
    }




}
