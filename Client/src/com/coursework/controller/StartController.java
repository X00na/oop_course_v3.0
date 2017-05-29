package com.coursework.controller;

import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.bean.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    @FXML private TextField login;
    @FXML private PasswordField pass;
    @FXML private Button enter;
    @FXML private Label error;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enter.setOnAction((e) -> {
            ResourceBundle rsBnd = ResourceBundle.getBundle("server");
            try(Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                Request request = new Request();
                request.setCommand("login");
                User user = new User();
                user.setLogin(login.getText());
                user.setPassword(pass.getText());
                request.setAttribute("user", user);

                out.writeObject(request);
                Response response = (Response) in.readObject();

                if(!response.isError()){
                    this.loadUser((User) response.getParameter("user"), (Stage) enter.getScene().getWindow());
                } else {
                    error.setVisible(true);
                    pass.setText("");
                }
            } catch(IOException | ClassNotFoundException exc){
                exc.printStackTrace();
                error.setVisible(true);
            }
        });
    }

    private void loadUser(User user, Stage oldWindow) throws IOException {
        if(user.isRole()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"));
            Stage newWindow = new Stage();
            newWindow.setTitle("Окно администратора");
            VBox layout = loader.load();
            newWindow.setScene(new Scene(layout));
            newWindow.show();
            oldWindow.close();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user.fxml"));
            Stage newWindow = new Stage();
            newWindow.setTitle("Окно пользователя");
            VBox layout = loader.load();
            newWindow.setScene(new Scene(layout));
            newWindow.show();
            oldWindow.close();
        }
    }

    public void setFocus(){
        enter.requestFocus();
    }
}
