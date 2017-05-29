package com.coursework.dao;

import com.coursework.dao.exception.DAOException;

import java.util.List;

public interface ProductDAO<T, K> extends ShopDAO<T, K> {
    List<T> searchByName(String name) throws DAOException;
    List<T> searchByPrice(int price) throws DAOException;
    List<T> searchByType(String type) throws DAOException;
}
