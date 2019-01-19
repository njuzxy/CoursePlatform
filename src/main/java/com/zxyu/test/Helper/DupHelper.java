package com.zxyu.test.Helper;

import com.zxyu.test.Dao.FileDao;
import com.zxyu.test.Entity.SubmitEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DupHelper {
    @Autowired
    private FileDao fileDao;
    private int aid;
    private List<SubmitEntity> pySubmits;
    private List<SubmitEntity>javaSubmits;
    private List<SubmitEntity>scalaSubmits;

    public DupHelper(int aid){
        this.aid=aid;
        this.pySubmits=fileDao.getPySubmits(aid);
        this.javaSubmits=fileDao.getJavaSubmits(aid);
        this.scalaSubmits=fileDao.getScalaSubmits(aid);
    }

    public void calPySimilarity(){

    }




}
