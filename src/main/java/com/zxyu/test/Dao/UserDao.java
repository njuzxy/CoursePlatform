package com.zxyu.test.Dao;

import com.zxyu.test.Entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
     void addUser(UserEntity userEntity);

     void removeUser(String email);

     void updateUser(UserEntity userEntity);

     UserEntity findUser(String email);


}
