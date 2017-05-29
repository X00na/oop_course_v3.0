package com.coursework;

import com.coursework.bean.Order;
import com.coursework.bean.Product;
import com.coursework.bean.Type;
import com.coursework.bean.User;
import com.coursework.dao.OrderDAO;
import com.coursework.dao.ProductDAO;
import com.coursework.dao.UserDAO;
import com.coursework.dao.connectionpool.ConnectionPool;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Foo {
    public static void main(String[] args) throws DAOException, ParseException {
        ConnectionPool.getInstance();
        try {
//            ProductDAO<Product, Integer> dao = DAOFactory.getInstance().getProductDao();
            UserDAO<User, Integer> dao = DAOFactory.getInstance().getUserDao();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*List<Order> list = dao.searchByDate(df.parse("2016-11-28"));
            list.forEach(System.out :: println);*/
            Product product = new Product();
//            Type type = new Type();
            Order order = new Order();
            order.setId(2);
            order.setCount(2222);
            order.setDateOrder(new Date());
            order.setInfo("dsdssssssssssssssdsd");
            order.setProduct(product);
//            type.setId(1);
            product.setId(2);
            product.setName("Холодильник LG");
//            product.setInfo("rrrr");
//            product.setCount(122134);
//            product.setPrice(314);
//            product.setDateCreate(new Date());
//            product.setType(type);
            System.out.println(dao.selectByLogin("admin"));
        } finally {
            ConnectionPool.getInstance().closeAll();
        }
    }
}
