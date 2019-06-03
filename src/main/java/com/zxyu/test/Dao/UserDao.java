package com.zxyu.test.Dao;

import com.zxyu.test.Entity.*;
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

     AssignmentEntity findAssignment(int aid);

     void updateAssignment(AssignmentEntity assignmentEntity);

     void addNotices(List<NoticeEntity>notices);

     void updateNotices(NoticeEntity notice);

     List<UserEntity> findAllUser();

     List<SubmitEntity> findSubmitsBySid(String sid);

     List<NoticeEntity> findNoticesBySid(String sid);

     List<DuplicateEntity> findDuplicate(int aid);
}
