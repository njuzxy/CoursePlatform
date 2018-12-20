package com.zxyu.test;

import com.zxyu.test.Dao.FileDao;
import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.AssignmentEntity;
import com.zxyu.test.Entity.DuplicateEntity;
import com.zxyu.test.Entity.SubmitEntity;
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

    @Autowired
    private FileDao fileDao;

    @Test
    public void testSaveUser() throws Exception {
        UserEntity user=new UserEntity();
        user.setPassword("123456");
        user.setSid("151250199");
        user.setUsername("zxyu");
        userDao.addUser(user);
    }
    @Test
    public void testUpdateUser()throws Exception{
        UserEntity user=new UserEntity();
        user.setPassword("123");
        user.setSid("151250199");
        user.setUsername("zxyu");
        userDao.updateUser(user);
    }

    @Test
    public void testFindUser()throws Exception{
        UserEntity userEntity=userDao.findUser("151250199");
        System.out.println(userEntity.getUsername());
    }

    @Test
    public void testRemoveUser()throws Exception{
        userDao.removeUser("151250199");
    }

    @Test
    public void testAddSubmit()throws Exception{
        SubmitEntity submitEntity=new SubmitEntity();
        submitEntity.setAid(1);
        submitEntity.setSid("151250199");
        submitEntity.setLanguage("java");
        userDao.addSubmit(submitEntity);
    }

    @Test
    public void testUpdateSubmit()throws Exception{
        SubmitEntity submitEntity=new SubmitEntity();
        submitEntity.setAid(1);
        submitEntity.setSid("151250199");
        submitEntity.setLanguage("scala");
        submitEntity.setType("test");
        userDao.updateSubmit(submitEntity);
    }

    @Test
    public void TestFindSubmit()throws Exception{
        SubmitEntity submitEntity=userDao.findSubmit("151250199",1);
        System.out.println(submitEntity.getLanguage());
    }

    @Test
    public void TestAddAssignment()throws Exception{
        AssignmentEntity assignmentEntity=new AssignmentEntity();
        assignmentEntity.setAid(1);
        assignmentEntity.setIntro("test");
        assignmentEntity.setTitle("作业1");
        userDao.addAssignment(assignmentEntity);
    }

    @Test
    public void TestAddDuplicate()throws Exception{
        DuplicateEntity duplicateEntity=new DuplicateEntity();
        duplicateEntity.setAid(1);
        duplicateEntity.setSid("151250199");
        fileDao.addDuplicate(duplicateEntity);
    }


}
