package com.zxyu.test.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
@Document(collection = "course")
public class CourseEntity implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    @Id
    private String ctype;

    public CourseEntity(String ctype) {
        this.ctype = ctype;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }
}
