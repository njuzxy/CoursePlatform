package com.zxyu.test.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
@Document(collection = "assignment")
public class AssignmentEntity implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    @Id
    private int aid;
    private String title;
    private String ctype;
    private String intro;
    private String deadline;
    private String file;
    private String testfile;

    public AssignmentEntity(String title, String ctype, String intro, String deadline, String file, String testfile) {
        this.title = title;
        this.ctype = ctype;
        this.intro = intro;
        this.deadline = deadline;
        this.file = file;
        this.testfile = testfile;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTestfile() {
        return testfile;
    }

    public void setTestfile(String testfile) {
        this.testfile = testfile;
    }
}
