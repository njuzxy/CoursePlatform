package com.zxyu.test.Dao;

import com.zxyu.test.Entity.DuplicateEntity;
import com.zxyu.test.Entity.SubmitEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FileDao {
    void addDuplicate(DuplicateEntity duplicateEntity);

    void updateDuplicate(DuplicateEntity duplicateEntity);

    List<SubmitEntity> getPySubmits(int aid);

    List<SubmitEntity> getJavaSubmits(int aid);

    List<SubmitEntity> getScalaSubmits(int aid);
}
