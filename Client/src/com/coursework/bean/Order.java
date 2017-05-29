package com.coursework.bean;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private static long serialVersionUID = 1L;

    private int id;
    private int count;
    private Date dateOrder;
    private String info;
    private Product product;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", count=" + count + ", dateOrder=" + dateOrder + ", info='" + info + '\'' + ", product=" + product + '}';
    }
}
