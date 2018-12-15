package com.zxyu.test.DaoImpl;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addUser(UserEntity userEntity) {
        mongoTemplate.save(userEntity);
    }

    @Override
    public void removeUser(String email) {
        Query query=new Query(Criteria.where("email").is(email));
        mongoTemplate.remove(query,UserEntity.class);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        Query query=new Query(Criteria.where("email").is(userEntity.getEmail()));
        Update update=new Update();
        update.set("username",userEntity.getUsername());
        update.set("password",userEntity.getPassword());
        update.set("sid",userEntity.getSid());
        mongoTemplate.updateFirst(query,update,UserEntity.class);
    }

    @Override
    public UserEntity findUser(String email) {
        Query query=new Query(Criteria.where("email").is(email));
        UserEntity userEntity=mongoTemplate.findOne(query,UserEntity.class);
        return userEntity;
    }
}
