package com.zxyu.test.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "a_submit")
@CompoundIndexes({
        @CompoundIndex(name = "submitIndex",def = "{'sid':1,'aid':1}")
})
public class SubmitEntity implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String sid;
    private int aid;
    private String language;
    private String state;
    private String file;
    private String text_file;
    private String type;
    private double run_score;
    private double quality_score;
    private Date time;


    public SubmitEntity(String sid, int aid, String language, String state, String file,String text_file, String type, double run_score, double quality_score, Date time) {
        this.sid = sid;
        this.aid = aid;
        this.language = language;
        this.state = state;
        this.file = file;
        this.text_file=text_file;
        this.type = type;
        this.run_score = run_score;
        this.quality_score = quality_score;
        this.time = time;
    }

    public String getText_file() {
        return text_file;
    }

    public void setText_file(String text_file) {
        this.text_file = text_file;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRun_score() {
        return run_score;
    }

    public void setRun_score(double run_score) {
        this.run_score = run_score;
    }

    public double getQuality_score() {
        return quality_score;
    }

    public void setQuality_score(double quality_score) {
        this.quality_score = quality_score;
    }
}
