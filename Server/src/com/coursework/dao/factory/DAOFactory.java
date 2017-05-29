package com.coursework.dao.factory;

import com.coursework.bean.Order;
import com.coursework.bean.Product;
import com.coursework.bean.Type;
import com.coursework.bean.User;
import com.coursework.dao.OrderDAO;
import com.coursework.dao.ProductDAO;
import com.coursework.dao.ShopDAO;
import com.coursework.dao.UserDAO;
import com.coursework.dao.impl.OrderDaoImpl;
import com.coursework.dao.impl.ProductDaoImpl;
import com.coursework.dao.impl.TypeDaoImpl;
import com.coursework.dao.impl.UserDaoImpl;

public class DAOFactory {
    private static DAOFactory instance = new DAOFactory();
    private ProductDaoImpl productDao = new ProductDaoImpl();
    private OrderDaoImpl orderDao = new OrderDaoImpl();
    private TypeDaoImpl typeDao = new TypeDaoImpl();
    private UserDaoImpl userDao = new UserDaoImpl();

    private DAOFactory(){}

    public static DAOFactory getInstance(){
        return instance;
    }

    public OrderDAO<Order, Integer> getOderDao(){
        return orderDao;
    }

    public ProductDAO<Product, Integer> getProductDao() {
        return productDao;
    }

    public ShopDAO<Type, Integer> getTypeDao() {
        return typeDao;
    }

    public UserDAO<User, Integer> getUserDao() {
        return userDao;
    }
}
