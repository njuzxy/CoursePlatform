package com.zxyu.test.Dao;

import com.zxyu.test.Entity.AssignmentEntity;
import com.zxyu.test.Entity.CourseEntity;
import com.zxyu.test.Entity.SubmitEntity;
import com.zxyu.test.Entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserDao {
     void addUser(UserEntity userEntity);

     void removeUser(String sid);

     void updateUser(UserEntity userEntity);

     UserEntity findUser(String sid);

     void addSubmit(SubmitEntity submitEntity);

     void updateSubmit(SubmitEntity submitEntity);

     SubmitEntity findSubmit(String sid,int aid);

     void addAssignment(AssignmentEntity assignmentEntity);

     void addCourseEntity(CourseEntity courseEntity);

     int findCourseNextAid();

     List<SubmitEntity> findAllSubmit(int aid);

     List<CourseEntity> findAllCourse();

     List<AssignmentEntity> findAllAssignment(String ctype);

}
