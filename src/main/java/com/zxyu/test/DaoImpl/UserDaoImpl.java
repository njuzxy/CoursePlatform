package com.zxyu.test.DaoImpl;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.AssignmentEntity;
import com.zxyu.test.Entity.CourseEntity;
import com.zxyu.test.Entity.SubmitEntity;
import com.zxyu.test.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addUser(UserEntity userEntity) {
        mongoTemplate.save(userEntity);
    }

    @Override
    public void removeUser(String sid) {
        Query query=new Query(Criteria.where("sid").is(sid));
        mongoTemplate.remove(query,UserEntity.class);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        Query query=new Query(Criteria.where("sid").is(userEntity.getSid()));
        Update update=new Update();
        update.set("username",userEntity.getUsername());
        update.set("password",userEntity.getPassword());
        mongoTemplate.updateFirst(query,update,UserEntity.class);
    }

    @Override
    public UserEntity findUser(String sid) {
        Query query=new Query(Criteria.where("sid").is(sid));
        UserEntity userEntity=mongoTemplate.findOne(query,UserEntity.class);
        return userEntity;
    }

    @Override
    public void addSubmit(SubmitEntity submitEntity) {
        mongoTemplate.save(submitEntity);
    }

    @Override
    public void updateSubmit(SubmitEntity submitEntity) {
        Query query=new Query(Criteria.where("sid").is(submitEntity.getSid()).and("aid").is(submitEntity.getAid()));
        Update update=new Update();
        update.set("language",submitEntity.getLanguage());
        update.set("state",submitEntity.getState());
        update.set("file",submitEntity.getFile());
        update.set("type",submitEntity.getType());
       mongoTemplate.updateFirst(query,update,SubmitEntity.class);
    }

    @Override
    public SubmitEntity findSubmit(String sid, int aid) {
        Query query=new Query(Criteria.where("sid").is(sid).and("aid").is(aid));
        SubmitEntity submitEntity=mongoTemplate.findOne(query,SubmitEntity.class);
        return submitEntity;
    }

    @Override
    public void addAssignment(AssignmentEntity assignmentEntity) {
        mongoTemplate.save(assignmentEntity);
    }

    @Override
    public void addCourseEntity(CourseEntity courseEntity) {
        mongoTemplate.save(courseEntity);
    }

    @Override
    public int findCourseNextAid() {
        Query query=new Query(Criteria.where("_class").is("com.zxyu.test.Entity.AssignmentEntity"));
        Sort sort=new Sort(new Sort.Order(Sort.Direction.DESC,"aid"));
        query.with(sort);
        List<AssignmentEntity> list=mongoTemplate.find(query,AssignmentEntity.class,"assignment");
        if(list.size()==0)
            return 0;
        else
            return list.get(0).getAid()+1;
    }

    @Override
    public List<SubmitEntity> findAllSubmit(int aid) {
        Query query=new Query(Criteria.where("aid").is(aid));
        List<SubmitEntity>result=mongoTemplate.find(query,SubmitEntity.class,"a_submit");
        return result;
    }
}
