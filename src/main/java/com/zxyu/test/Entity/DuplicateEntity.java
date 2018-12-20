package com.zxyu.test.Entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "duplicate")
@CompoundIndexes({
        @CompoundIndex(name = "duplicateIndex",def = "{'aid':1,'sid':1}")
})
public class DuplicateEntity implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private int aid;
    private String sid;
    private String similar_sid;
    private double s_percent;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSimilar_sid() {
        return similar_sid;
    }

    public void setSimilar_sid(String similar_sid) {
        this.similar_sid = similar_sid;
    }

    public double getS_percent() {
        return s_percent;
    }

    public void setS_percent(double s_percent) {
        this.s_percent = s_percent;
    }
}
