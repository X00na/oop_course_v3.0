package com.coursework.dao;

import com.coursework.bean.User;
import com.coursework.dao.exception.DAOException;

public interface UserDAO<T, K> extends ShopDAO<T, K> {
    User selectByLogin(String login) throws DAOException;
}
