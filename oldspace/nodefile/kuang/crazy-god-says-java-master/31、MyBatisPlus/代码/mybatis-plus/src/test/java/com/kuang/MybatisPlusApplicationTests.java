package com.kuang;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.mapper.UserMapper;
import com.kuang.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired(required=false)
    private UserMapper userMapper;

    //测试查询

    //测试插入
    @Test
    public void testInsert(){
        User user = new User();
        user.setName("李四");
        user.setAge(23);
        user.setEmail("1622840727@qq.com");

        int result = userMapper.insert(user);// 帮我们自动生成id
        System.out.println(result); // 受影响的行数
        System.out.println(user); // 发现，id会自动回填
    }

    //更新测试
    @Test
    public void testUpdate(){
        User user = new User();
        user.setId(5L);
        user.setName("王五");
        user.setAge(20);
        int i = userMapper.updateById(user);
        System.out.println(i);
    }
    // 测试乐观锁成功！
    @Test public void testOptimisticLocker(){
        // 1、查询用户信息
        User user = userMapper.selectById(1L);
        // 2、修改用户信息
        user.setName("kuangshen");
        user.setEmail("12346789@qq.com");
        // 3、执行更新操作
        userMapper.updateById(user);
    }

    // 测试乐观锁失败！多线程下
    @Test public void testOptimisticLocker2(){

        // 线程 1
        User user = userMapper.selectById(1L);
        user.setName("kuangshen111");
        user.setEmail("12346789@qq.com");
        // 模拟另外一个线程执行了插队操作
        User user2 = userMapper.selectById(1L);
        user2.setName("kuangshen222");
        user2.setEmail("987654321@qq.com");

        userMapper.updateById(user2);
        // 自旋锁来多次尝试提交！
        userMapper.updateById(user); // 如果没有乐观锁就会覆盖插队线程的值！
    }
    //测试查找
    @Test
    public void testSelectById(){
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }
    // 测试批量查询！
    @Test
    public void testSelectByBatchId(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }
    // 按条件查询之一使用map操作
    @Test
    public void testSelectBatchIds(){
        HashMap<String,Object> map = new HashMap<>();
        // 自定义要查询
        map.put("name","Tom");
        map.put("age",28);

        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }
    @Test
    public void testPage(){
        //参数一：当前页
        //参数二：页面大小
        //使用了分页插件之后，所有的分页操作也变得简单了！
        Page<User> page = new Page<>(2, 5);
        userMapper.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
    }

    @Test
    //测试删除
    public void testDeleteById(){
        userMapper.deleteById(1L);
    }

    @Test
    //通过id批量删除
    public void testDeleteBatcId(){
        userMapper.deleteBatchIds(Arrays.asList(1398854420468363266L,1398854420468363267L,1398854420468363268L));
    }

    @Test
    public void testDeleteMap(){
       HashMap<String,Object> map = new HashMap<>();
       map.put("name","张三");
       userMapper.deleteByMap(map);
    }

    @Test
    public void contextLoads(){
        // 查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于12
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .isNotNull("name")   //name不为空
                .isNotNull("email")  //email不为空
                .ge("age",20);
        userMapper.selectList(wrapper).forEach(System.out::println);

    }

    @Test
    void test2(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name","Tom");
        User user = userMapper.selectOne(wrapper); // 查询一个数据，出现多个结果使用List 或者 Map
        System.out.println(user);
    }

    @Test
    void test3(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age",20,30);
        Integer count = userMapper.selectCount(wrapper);// 查询结果数
        System.out.println(count);
    }


    @Test
    void tes4(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper
                .notLike("name","e")
                .likeRight("email","t");
        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        maps.forEach(System.out::println);
    }

    @Test
    void test5(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.inSql("id","select id from user where id<3");
        List<Object> objects = userMapper.selectObjs(wrapper);
        objects.forEach(System.out::println);

    }
}
