package com.coursework.controller;

import com.coursework.bean.Product;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.util.CommandHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, String> infoCol;
    @FXML private TableColumn<Product, Integer> countCol;
    @FXML private TableColumn<Product, Integer> priceCol;
    @FXML private TableColumn<Product, Date> dateCol;
    @FXML private TableColumn<Product, String> typeCol;
    @FXML private ChoiceBox<String> choice;
    @FXML private TextField search;
    @FXML private Button button;
    @FXML private Button reset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        infoCol.setCellValueFactory(new PropertyValueFactory<>("info"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreate"));
        typeCol.setCellValueFactory((cellData) -> new SimpleObjectProperty<>(cellData.getValue().getType().getName()));

        choice.getItems().addAll("Названию", "Цене", "Типу");
        choice.getSelectionModel().selectFirst();
        search.setOnKeyPressed((ke) -> {
            if(ke.getCode().equals(KeyCode.ENTER)){
                this.searchHandler();
            }
        });
        button.setOnAction((e) -> this.searchHandler());
        reset.setOnAction((e) -> {
            this.loadTableData();
            search.setText("");
        });

        this.loadTableData();
    }

    private void loadTableData() {
        table.setItems(FXCollections.observableList(CommandHelper.loadListData("allProducts", "products")));
    }

    private void searchHandler(){
        String searchValue = search.getText();
        if(searchValue.isEmpty()){
            this.loadTableData();
        } else {
            table.setItems(FXCollections.observableArrayList(CommandHelper.search("searchProduct",
                    choice.getSelectionModel().getSelectedItem(), searchValue, "products")));
        }
    }
}
