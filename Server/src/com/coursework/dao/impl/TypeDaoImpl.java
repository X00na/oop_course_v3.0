package com.coursework.dao.impl;

import com.coursework.bean.Type;
import com.coursework.dao.ShopDAO;
import com.coursework.dao.connectionpool.ConnectionPool;
import com.coursework.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TypeDaoImpl implements ShopDAO<Type, Integer> {
    private static final String SELECT = "SELECT id, name FROM types;";
    private static final String INSERT = "INSERT INTO types (name) VALUES(?);";
    private static final String UPDATE = "UPDATE types SET name = ? WHERE id = ?;";
    private static final String DELETE = "DELETE FROM types WHERE id = ?";

    @Override
    public List<Type> select() throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Type> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()){
            try(ResultSet rs = statement.executeQuery(SELECT)){
                while(rs.next()){
                    Type type = new Type();
                    type.setId(rs.getInt(1));
                    type.setName(rs.getString(2));
                    list.add(type);
                }
            }
        } catch(SQLException e){
            throw new DAOException("Ошибка SQL " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnToPool(connection);
        }
        return list;
    }

    @Override
    public int insert(Type bean) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        int key = 0;
        try(PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, bean.getName());

            statement.executeUpdate();

            try(ResultSet rs = statement.getGeneratedKeys()){
                if(rs.next()){
                    key = rs.getInt(1);
                }
            }
        } catch(SQLException e){
            throw new DAOException("Ошибка SQL " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnToPool(connection);
        }
        return key;
    }

    @Override
    public void update(Type bean) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(UPDATE)){
            statement.setString(1, bean.getName());
            statement.setInt(2, bean.getId());

            statement.executeUpdate();
        } catch(SQLException e){
            throw new DAOException("Ошибка SQL " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnToPool(connection);
        }
    }

    @Override
    public void delete(Integer id) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(DELETE)){
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch(SQLException e){
            throw new DAOException("Ошибка SQL " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnToPool(connection);
        }
    }
}
