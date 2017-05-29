package com.coursework.bean;

import java.io.Serializable;

public class Type implements Serializable {
    private static long serialVersionUID = 1L;

    private int id;
    private String name;

    public Type() {
    }

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

    @Override
    public String toString() {
        return "Type{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
