package com.coursework.dao;

import com.coursework.dao.exception.DAOException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDAO<T, K> extends ShopDAO<T, K> {
    List<T> searchByDate(Date date) throws DAOException;
    List<T> searchByProduct(String name) throws DAOException;
    Map<String, Integer> selectStatistic() throws DAOException;
}
