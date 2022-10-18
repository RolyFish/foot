package com.kuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.pojo.User;
import org.springframework.web.bind.annotation.ResponseBody;

//在对应的mapper上面继承基本的接口BaseMapper
@ResponseBody //代表持久层
public interface UserMapper extends BaseMapper<User> {

}
