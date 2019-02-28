package com.zxyu.test.Entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "notice")
@CompoundIndexes({
        @CompoundIndex(name = "noticeIndex",def = "{'sid':1,'aid':1}")
})
public class NoticeEntity implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private int aid;
    private String sid;
    private Date time;
    private String text;
    private String state;

    public NoticeEntity(int aid,String sid,Date time,String text,String state){
        this.aid=aid;
        this.time=time;
        this.sid=sid;
        this.text=text;
        this.state=state;
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
