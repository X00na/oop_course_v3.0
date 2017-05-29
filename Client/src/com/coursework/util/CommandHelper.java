package com.coursework.util;

import com.coursework.bean.Request;
import com.coursework.bean.Response;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class CommandHelper {
    public static <T> List<T> loadListData(String command, String expected){
        ResourceBundle rsBnd = ResourceBundle.getBundle("server");
        try(Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request request = new Request();
            request.setCommand(command);

            out.writeObject(request);
            Response response = (Response) in.readObject();

            if(!response.isError()){
                return (List<T>) response.getParameter(expected);
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static <T> List<T> search(String command, String searchType, String searchValue, String expected){
        ResourceBundle rsBnd = ResourceBundle.getBundle("server");
        try(Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request request = new Request();
            request.setCommand(command);
            request.setAttribute("searchType", searchType);

            request.setAttribute("searchValue", searchValue);

            out.writeObject(request);
            Response response = (Response) in.readObject();

            if(!response.isError()){
                return (List<T>) response.getParameter(expected);
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static <T> boolean query(String command, String name, T bean){
        ResourceBundle rsBnd = ResourceBundle.getBundle("server");
        try(Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request request = new Request();
            request.setCommand(command);
            request.setAttribute(name, bean);

            out.writeObject(request);
            Response response = (Response) in.readObject();

            if(!response.isError()){
                return true;
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }
}
