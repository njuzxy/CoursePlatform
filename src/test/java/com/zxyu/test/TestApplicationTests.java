package com.zxyu.test;

import com.zxyu.test.Dao.FileDao;
import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.*;
import com.zxyu.test.Helper.MyLauncher;
import com.zxyu.test.Helper.RemoteCmd;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {
    @Autowired
    private UserDao userDao;

    @Autowired
    private FileDao fileDao;

    @Test
    public void testSaveUser() throws Exception {
        UserEntity user=new UserEntity("151250199","zxyu","123456");
        userDao.addUser(user);
    }
    @Test
    public void testUpdateUser()throws Exception{
        UserEntity user=new UserEntity("151250199","zxyu","123");
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
        SubmitEntity submitEntity=new SubmitEntity("151250199",1,"java","已提交","filepath","云计算","0","0",new Date());
        userDao.addSubmit(submitEntity);
    }

    @Test
    public void testUpdateSubmit()throws Exception{
        SubmitEntity submitEntity=new SubmitEntity("151250199",1,"scala","已提交","filepath","云计算","0","0",new Date());
        userDao.updateSubmit(submitEntity);
    }

    @Test
    public void TestFindSubmit()throws Exception{
        SubmitEntity submitEntity=userDao.findSubmit("151250199",1);
        System.out.println(submitEntity.getLanguage());
    }

    @Test
    public void TestAddAssignment()throws Exception{
        AssignmentEntity assignmentEntity=new AssignmentEntity(userDao.findCourseNextAid(),"assignment01","云计算","简介","test","file","testfile");
        userDao.addAssignment(assignmentEntity);
    }

    @Test
    public void TestAddDuplicate()throws Exception{
        DuplicateEntity duplicateEntity=new DuplicateEntity(1,"151250199","151250200",0.3);
        fileDao.addDuplicate(duplicateEntity);
    }

    @Test
    public void TestAddCourseEntity()throws Exception{
        CourseEntity courseEntity=new CourseEntity("云计算");
        userDao.addCourseEntity(courseEntity);
    }

    @Test
    public void TestFindCourseNextId()throws Exception{
        System.out.println(userDao.findCourseNextAid());
    }

    @Test
    public void TestFindAllSubmit()throws Exception{
        List<SubmitEntity>list=userDao.findAllSubmit(1);
        System.out.println(list.size());
    }

    @Test
    public void TestFindAllCourse()throws Exception{
        List<CourseEntity>list=userDao.findAllCourse();
        System.out.println(list.size());
    }

    @Test
    public void TestFindAllAssignment()throws Exception{
        List<AssignmentEntity>list=userDao.findAllAssignment("云计算");
        System.out.println(list.size());
    }

    @Test
    public void TestMyLauncher()throws Exception{
        String[]input=new String[2];
        input[0]="python";
        input[1]="i:\\testpy.py";
        MyLauncher.main(input);
    }

    @Test
    public void TestRemoteCmd()throws Exception{
        String[]input=new String[2];
        input[0]="python";
        input[1]="/home/spark/file/testpy.py";
        RemoteCmd.main(input);
    }


}
