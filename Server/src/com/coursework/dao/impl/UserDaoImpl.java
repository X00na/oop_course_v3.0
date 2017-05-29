package com.coursework.dao.impl;

import com.coursework.bean.User;
import com.coursework.dao.UserDAO;
import com.coursework.dao.connectionpool.ConnectionPool;
import com.coursework.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDAO<User, Integer> {
    private static final String SELECT_BY_LOGIN = "SELECT id, login, password, role FROM users WHERE login = ?;";

    @Override
    public List<User> select() throws DAOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public User selectByLogin(String login) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        User user = null;
        try(PreparedStatement statement = connection.prepareStatement(SELECT_BY_LOGIN)){
            statement.setString(1, login);

            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    user = new User();
                    user.setId(rs.getInt(1));
                    user.setLogin(rs.getString(2));
                    user.setPassword(rs.getString(3));
                    user.setRole(rs.getBoolean(4));
                }
            }
        } catch(SQLException e){
            throw new DAOException("Ошибка SQL " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnToPool(connection);
        }
        return user;
    }

    @Override
    public int insert(User bean) throws DAOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(User bean) throws DAOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer key) throws DAOException {
        throw new UnsupportedOperationException();
    }
}
