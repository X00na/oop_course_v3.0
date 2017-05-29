package com.coursework.bean;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
    private static long serialVersionUID = 1L;

    private int id;
    private String name;
    private String info;
    private int count;
    private int price;
    private Date dateCreate;
    private Type type;

    public Product() {
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name='" + name + '\'' + ", info='" + info + '\'' + ", count=" + count + ", price=" + price + ", dateCreate=" + dateCreate + ", type=" + type + '}';
    }
}
