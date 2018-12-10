package com.zxyu.test.DaoImpl;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addUser(UserEntity userEntity) {
        mongoTemplate.save(userEntity);
    }
}
