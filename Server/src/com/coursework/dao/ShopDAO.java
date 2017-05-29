package com.coursework.dao;

import com.coursework.dao.exception.DAOException;

import java.util.List;

public interface ShopDAO<T, K> {
    List<T> select() throws DAOException;
    int insert(T bean) throws DAOException;
    void update(T bean) throws DAOException;
    void delete(K key) throws DAOException;
}
