package com.coursework;

import com.coursework.dao.exception.DAOException;
import com.coursework.server.Server;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args){
        try{
            Server server = new Server();
            server.startupServer();
        } catch(IOException | DAOException e){
            System.out.println("Сервер не запущен");
        }
    }
}
