package com.coursework;

import com.coursework.controller.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Start extends Application {

    @Override
    public void start(Stage window) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/start.fxml"));
        VBox layout = loader.load();
        StartController controller = loader.getController();
        window.setTitle("Онлайн-магазин");
        window.setScene(new Scene(layout));
        controller.setFocus();
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
