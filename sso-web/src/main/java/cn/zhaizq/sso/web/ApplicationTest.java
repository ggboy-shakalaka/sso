package cn.zhaizq.sso.web;

import cn.zhaizq.sso.service.domain.entry.User;
import cn.zhaizq.sso.service.mapper.UserMapper;
import com.ggboy.framework.utils.redis.RedisWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Autowired
    private RedisWrapper redisWrapper;

    @Test
    public void redisTest() {
        redisWrapper.set("Test:001", null);
//        redisWrapper.expire("Test:001", 90000);
    }
}