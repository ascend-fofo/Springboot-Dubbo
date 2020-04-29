package com.kgc.cn.common.vo;

import java.io.Serializable;

public class UserVo implements Serializable {
    private static final long serialVersionUID = -6730171894489924743L;
    private int id;
    private String name;
    private int sex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
