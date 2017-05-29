package com.coursework.dao.connectionpool;

import com.coursework.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> busyConnections;
    private static Lock lock = new ReentrantLock();
    private static ConnectionPool instance;

    public static ConnectionPool getInstance() throws DAOException {
        lock.lock();
        try{
            if(instance == null){
                instance = new ConnectionPool();
            }
        } finally{
            lock.unlock();
        }
        return instance;
    }

    private ConnectionPool() throws DAOException{
        ResourceBundle dbRes = ResourceBundle.getBundle("mysqldatabase");
        int poolSize = Integer.parseInt(dbRes.getString("db.poolsize"));
        freeConnections = new ArrayBlockingQueue<>(poolSize);
        busyConnections = new ArrayBlockingQueue<>(poolSize);

        try {
            Class.forName(dbRes.getString("db.driver"));

            for(int i = 0; i < poolSize; i++){
                Connection connection = DriverManager.getConnection(
                        dbRes.getString("db.url"),
                        dbRes.getString("db.user"),
                        dbRes.getString("db.password"));
                freeConnections.add(connection);
            }
        } catch(ClassNotFoundException | SQLException e){
            throw new DAOException(e);
        }
    }

    public Connection getConnection() throws DAOException{
        Connection connection = null;
        try {
            connection = freeConnections.take();
            busyConnections.add(connection);
        } catch(InterruptedException e){
            throw new DAOException(e.getMessage(), e);
        }
        return connection;
    }

    public void returnToPool(Connection connection) throws DAOException{
        if(busyConnections.contains(connection)){
            try {
                freeConnections.put(connection);
                busyConnections.remove(connection);
            } catch(InterruptedException e){
                throw new DAOException(e.getMessage(), e);
            }
        } else {
            throw new DAOException("Попытка вернуть в пул не его соединение");
        }
    }

    public void closeAll() throws DAOException{
        try{
            for(Connection freeConnection : freeConnections){
                freeConnection.close();
            }
            for(Connection busyConnection : busyConnections){
                busyConnection.close();
            }
        } catch(SQLException e){
            throw new DAOException("Ошибка закрытия соединения");
        }
    }
}
