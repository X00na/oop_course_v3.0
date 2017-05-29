package com.coursework.dao.impl;

import com.coursework.bean.Order;
import com.coursework.bean.Product;
import com.coursework.dao.OrderDAO;
import com.coursework.dao.connectionpool.ConnectionPool;
import com.coursework.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoImpl implements OrderDAO<Order, Integer> {
    private static final String SELECT = "SELECT o.id, o.count, o.date_order, o.info, p.id, p.name " +
            "FROM orders AS o INNER JOIN products AS p ON o.product_id = p.id;";
    private static final String INSERT = "INSERT INTO orders (count, date_order, info, product_id) " +
            "SELECT ?, ?, ?, (SELECT id FROM products WHERE name = ?);";
    private static final String UPDATE = "UPDATE orders SET count = ?, date_order = ?, info = ?, " +
            "product_id = (SELECT id FROM products WHERE name = ?) WHERE id = ?;";
    private static final String DELETE = "DELETE FROM orders WHERE id = ?";
    private static final String SEARCH_BY_PRODUCT = "SELECT o.id, o.count, o.date_order, o.info, p.id, p.name " +
            "FROM orders AS o INNER JOIN products AS p ON o.product_id = p.id WHERE p.name LIKE ?;";
    private static final String SEARCH_BY_DATE = "SELECT o.id, o.count, o.date_order, o.info, p.id, p.name " +
            "FROM orders AS o INNER JOIN products AS p ON o.product_id = p.id WHERE DATE(o.date_order) = ?;";
    private static final String STATISTICS = "SELECT products.name, COUNT(products.id) FROM orders " +
            "INNER JOIN products ON orders.product_id = products.id " +
            "GROUP BY products.id;";

    @Override
    public List<Order> select() throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()){
            try(ResultSet rs = statement.executeQuery(SELECT)){
                while(rs.next()){
                    list.add(this.setOrder(rs));
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
    public int insert(Order bean) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        int key = 0;
        try(PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, bean.getCount());
            statement.setTimestamp(2, new Timestamp(bean.getDateOrder().getTime()));
            statement.setString(3, bean.getInfo());
            statement.setString(4, bean.getProduct().getName());

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
    public void update(Order bean) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(UPDATE)){
            statement.setInt(1, bean.getCount());
            statement.setTimestamp(2, new Timestamp(bean.getDateOrder().getTime()));
            statement.setString(3, bean.getInfo());
            statement.setString(4, bean.getProduct().getName());
            statement.setInt(5, bean.getId());

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
    public List<Order> searchByProduct(String name) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> list = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SEARCH_BY_PRODUCT)){
            statement.setString(1, "%" + name + "%");

            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    list.add(this.setOrder(rs));
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
    public List<Order> searchByDate(Date date) throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> list = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SEARCH_BY_DATE)){
            statement.setDate(1, new java.sql.Date(date.getTime()));

            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    list.add(this.setOrder(rs));
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
    public Map<String, Integer> selectStatistic() throws DAOException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Map<String, Integer> map = new HashMap<>();
        try(Statement statement = connection.createStatement()){
            try(ResultSet rs = statement.executeQuery(STATISTICS)){
                while(rs.next()){
                    map.put(rs.getString(1), rs.getInt(2));
                }
            }
        } catch(SQLException e){
            throw new DAOException("Ошибка SQL " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnToPool(connection);
        }
        return map;
    }

    private Order setOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        Product product = new Product();
        order.setId(rs.getInt(1));
        order.setCount(rs.getInt(2));
        order.setDateOrder(rs.getTimestamp(3));
        order.setInfo(rs.getString(4));
        product.setId(rs.getInt(5));
        product.setName(rs.getString(6));
        order.setProduct(product);

        return order;
    }
}
