package com.zxyu.test.Dao;

import com.zxyu.test.Entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
    public void addUser(UserEntity userEntity);
}
