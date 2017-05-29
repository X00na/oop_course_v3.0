package com.coursework.controller;

import com.coursework.Updateable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML private VBox layout;
    @FXML private MenuBar menu;
    @FXML private MenuItem product;
    @FXML private MenuItem type;
    @FXML private MenuItem order;
    @FXML private MenuItem circle;
    @FXML private MenuItem bar;
    private HashMap<String, Updateable> panels = new HashMap<>();
    private MenuItem current;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/products.fxml"));
            loader.load();
            Updateable updateable = loader.getController();
            updateable.update();
            panels.put(product.getText(), updateable);
            product.setDisable(true);
            current = product;

            loader = new FXMLLoader(getClass().getResource("/fxml/types.fxml"));
            loader.load();
            updateable = loader.getController();
            panels.put(type.getText(), updateable);

            loader = new FXMLLoader(getClass().getResource("/fxml/orders.fxml"));
            loader.load();
            updateable = loader.getController();
            panels.put(order.getText(), updateable);

            loader = new FXMLLoader(getClass().getResource("/fxml/statisticBar.fxml"));
            loader.load();
            updateable = loader.getController();
            panels.put(bar.getText(), updateable);

            loader = new FXMLLoader(getClass().getResource("/fxml/statisticCircle.fxml"));
            loader.load();
            updateable = loader.getController();
            panels.put(circle.getText(), updateable);
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        layout.getChildren().add(panels.get(product.getText()).getNode());

        product.setOnAction((e) -> this.change(product));
        type.setOnAction((e) -> this.change(type));
        order.setOnAction((e) -> this.change(order));
        circle.setOnAction((e) -> this.change(circle));
        bar.setOnAction((e) -> this.change(bar));
    }

    private void change(MenuItem item){
        layout.getChildren().clear();
        Updateable updateable = panels.get(item.getText());
        updateable.update();
        layout.getChildren().addAll(menu, updateable.getNode());
        current.setDisable(false);
        current = item;
        current.setDisable(true);
    }
}
