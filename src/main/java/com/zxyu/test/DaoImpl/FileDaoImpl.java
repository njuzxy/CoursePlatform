package com.zxyu.test.DaoImpl;

import com.zxyu.test.Dao.FileDao;
import com.zxyu.test.Entity.DuplicateEntity;
import com.zxyu.test.Entity.SubmitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileDaoImpl implements FileDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addDuplicate(DuplicateEntity duplicateEntity) {
        mongoTemplate.save(duplicateEntity);
    }

    @Override
    public void updateDuplicate(DuplicateEntity duplicateEntity) {
        Query query=new Query(Criteria.where("aid").is(duplicateEntity.getAid()).and("sid").is(duplicateEntity.getSid()
        ).and("similar_sid").is(duplicateEntity.getSimilar_sid()));
        Update update=new Update();
        update.set("s_percent",duplicateEntity.getS_percent());
        mongoTemplate.updateFirst(query,update,DuplicateEntity.class);
    }

    @Override
    public List<SubmitEntity> getPySubmits(int aid) {
        Query query=new Query(Criteria.where("aid").is(aid).and("language").is("python"));
        List<SubmitEntity>list=mongoTemplate.find(query,SubmitEntity.class,"a_submit");
        return list;
    }

    @Override
    public List<SubmitEntity> getJavaSubmits(int aid) {
        Query query=new Query(Criteria.where("aid").is(aid).and("language").is("java"));
        List<SubmitEntity>list=mongoTemplate.find(query,SubmitEntity.class,"a_submit");
        return list;
    }

    @Override
    public List<SubmitEntity> getScalaSubmits(int aid) {
        Query query=new Query(Criteria.where("aid").is(aid).and("language").is("scala"));
        List<SubmitEntity>list=mongoTemplate.find(query,SubmitEntity.class,"a_submit");
        return list;
    }
}
