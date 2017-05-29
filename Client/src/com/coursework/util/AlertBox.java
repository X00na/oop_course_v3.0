package com.coursework.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String title, String message){
        Stage window = new Stage();
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);

        Label label = new Label(message);
        Button ok = new Button();
        ok.setText("Ok");
        ok.setOnAction(e -> window.close());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, ok);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();
    }
}
