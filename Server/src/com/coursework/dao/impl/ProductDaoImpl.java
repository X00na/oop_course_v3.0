package com.coursework.dao.impl;

import com.coursework.bean.Product;
import com.coursework.bean.Type;
import com.coursework.dao.ProductDAO;
import com.coursework.dao.connectionpool.ConnectionPool;
import com.coursework.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDAO<Product, Integer> {
    private static final String SELECT = "SELECT p.id, p.name, p.info, p.count, p.price, p.date_create, " +
            "t.id, t.name FROM products AS p INNER JOIN types AS t ON p.type_id = t.id;";
    private static final String INSERT = "INSERT INTO products (name, info, count, price, date_create, type_id) " +
            "SELECT ?, ?, ?, ?, ?, (SELECT id FROM types WHERE name = ?);";
    private static final String UPDATE = "UPDATE products SET name = ?, info = ?, count = ?, price = ?, " +
            "date_create = ?, type_id = (SELECT id FROM types WHERE name = ?) WHERE id = ?;";
    private static final String DELETE = "DELETE FROM products WHERE id = ?";
    private static final String SEARCH_BY_NAME = "SELECT p.id, p.name, p.info, p.count, p.price, p.date_create, " +
            "t.id, t.name FROM products AS p INNER JOIN types AS t ON p.type_id = t.id " +
            "WHERE p.name LIKE ?;";
    private static final String SEARCH_BY_TYPE = "SELECT p.id, p.name, p.info, p.count, p.price, p.date_create, " +
            "t.id, t.name FROM products AS p INNER JOIN types AS t ON p.type_id = t.id " +
            "WHERE t.name LIKE ?;";
    private static final String SEARCH_BY_PRICE = "SELECT p.id, p.name, p.info, p.count, p.price, p.date_create, " +
            "t.id, t.name FROM products AS p INNER JOIN types AS t ON p.type_id = t.id " +
            "WHERE p.price = ?;";

    @Override
    public List<Product> select() throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Product> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()){
            try(ResultSet rs = statement.executeQuery(SELECT)){
                while(rs.next()){
                    list.add(this.setProduct(rs));
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
    public int insert(Product bean) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        int key = 0;
        try(PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, bean.getName());
            statement.setString(2, bean.getInfo());
            statement.setInt(3, bean.getCount());
            statement.setInt(4, bean.getPrice());
            statement.setTimestamp(5, new Timestamp(bean.getDateCreate().getTime()));
            statement.setString(6, bean.getType().getName());

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
    public void update(Product bean) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(UPDATE)){
            statement.setString(1, bean.getName());
            statement.setString(2, bean.getInfo());
            statement.setInt(3, bean.getCount());
            statement.setInt(4, bean.getPrice());
            statement.setTimestamp(5, new Timestamp(bean.getDateCreate().getTime()));
            statement.setString(6, bean.getType().getName());
            statement.setInt(7, bean.getId());

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

    @Override
    public List<Product> searchByName(String name) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Product> list = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SEARCH_BY_NAME)){
            statement.setString(1, "%" + name + "%");

            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    list.add(this.setProduct(rs));
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
    public List<Product> searchByType(String type) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Product> list = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SEARCH_BY_TYPE)){
            statement.setString(1, "%" + type + "%");

            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    list.add(this.setProduct(rs));
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
    public List<Product> searchByPrice(int price) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Product> list = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SEARCH_BY_PRICE)){
            statement.setInt(1, price);

            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    list.add(this.setProduct(rs));
                }
            }
        } catch(SQLException e){
            throw new DAOException("Ошибка SQL " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnToPool(connection);
        }
        return list;
    }

    private Product setProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt(1));
        product.setName(rs.getString(2));
        product.setInfo(rs.getString(3));
        product.setCount(rs.getInt(4));
        product.setPrice(rs.getInt(5));
        product.setDateCreate(rs.getTimestamp(6));
        Type type = new Type();
        type.setId(rs.getInt(7));
        type.setName(rs.getString(8));
        product.setType(type);

        return product;
    }
}
