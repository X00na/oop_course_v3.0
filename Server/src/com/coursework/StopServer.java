package com.coursework;

import com.coursework.bean.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ResourceBundle;

public class StopServer {
    public static void main(String[] args) {
        ResourceBundle rsBnd = ResourceBundle.getBundle("server");
        ResourceBundle bundle = ResourceBundle.getBundle("stopserver");

        try(Socket close = new Socket("localhost", Integer.parseInt(bundle.getString("port")));
            Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request request = new Request();
            request.setCommand("logout");
            out.writeObject(request);
            in.readObject();

        } catch(IOException | ClassNotFoundException e){
            System.out.println("Ошибка инициализации");
            System.exit(1);
        }
    }
}
