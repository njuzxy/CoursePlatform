package com.zxyu.test.DaoImpl;

import com.zxyu.test.Dao.FileDao;
import com.zxyu.test.Entity.DuplicateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class FileDaoImpl implements FileDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addDuplicate(DuplicateEntity duplicateEntity) {
        mongoTemplate.save(duplicateEntity);
    }
}
