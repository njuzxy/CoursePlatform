package com.zxyu.test.Dao;

import com.zxyu.test.Entity.DuplicateEntity;
import org.springframework.stereotype.Component;

@Component
public interface FileDao {
    void addDuplicate(DuplicateEntity duplicateEntity);
}
