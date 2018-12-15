package com.zxyu.test;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {
    @Autowired
    private UserDao userDao;

    @Test
    public void testSaveUser() throws Exception {
        UserEntity user=new UserEntity();
        user.setEmail("123456@qq.com");
        user.setPassword("123456");
        user.setSid("151250199");
        user.setUsername("zxyu");
        userDao.addUser(user);
    }
    @Test
    public void testUpdateUser()throws Exception{
        UserEntity user=new UserEntity();
        user.setEmail("123456@qq.com");
        user.setPassword("123");
        user.setSid("151250199");
        user.setUsername("zxyu");
        userDao.updateUser(user);
    }

    @Test
    public void testFindUser()throws Exception{
        UserEntity userEntity=userDao.findUser("123456@qq.com");
        System.out.println(userEntity.getUsername());
    }

    @Test
    public void testRemoveUser()throws Exception{
        userDao.removeUser("123456@qq.com");
    }


}
