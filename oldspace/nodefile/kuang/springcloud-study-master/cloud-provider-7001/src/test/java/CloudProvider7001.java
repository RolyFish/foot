import com.example.fuzz.service.UserService;
import com.example.fuzz.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/22 17:32
 */
@SpringBootTest
public class CloudProvider7001 {
    @Autowired
    private UserService userService;

    @Test
    public void test1() {
        List<User> users = userService.selectList();
        System.out.println(users);
    }

}
