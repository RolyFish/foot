package com.roily.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class JDBCController {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/jdbc")
    public List<Map<String, Object>> query(){
        String sql = " select * from users ";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
        return list_maps;
    }
    @RequestMapping("/insert")
    public List<Map<String, Object>> add(){
        String sql = " insert into users(id,name,pwd) values (5,'小米','123')";
        jdbcTemplate.update(sql);
        String sql2 = " select * from users ";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql2);
        return list_maps;
    } @RequestMapping("/update/{id}/{name}")
    public List<Map<String, Object>> update(@PathVariable("id") int id,@PathVariable("name")String name){
        String sql = " update  users set name=? where id=?";
        Object[] params = new Object[2];
        params[0]=name;
        params[1]=id;
        jdbcTemplate.update(sql,params);

        String sql2 = " select * from users ";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql2);
        return list_maps;
    }
    @RequestMapping("/del/{id}")
    public List<Map<String, Object>> del(@PathVariable("id")int id){
        String sql = " delete from users where id=?";
        jdbcTemplate.update(sql,id);

        String sql2 = " select * from users ";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql2);
        return list_maps;
    }
}
