package com.coursework.controller;

import com.coursework.Updateable;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.bean.Type;
import com.coursework.util.AlertBox;
import com.coursework.util.Checker;
import com.coursework.util.CommandHelper;
import com.coursework.util.GUIHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class TypeController implements Initializable, Updateable {
    @FXML private VBox layout;
    @FXML private TableView<Type> table;
    @FXML private TableColumn<Type, Integer> idCol;
    @FXML private TableColumn<Type, String> typeCol;
    @FXML private TextField newType;
    @FXML private Button add;
    @FXML private Button delete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.setEditable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            if(!Checker.validateString(neew)){
                AlertBox.display("Ошибка", "Недопустимое название типа");
                TableView<Type> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            } else {
                Type type = value.getTableView().getSelectionModel().getSelectedItem();
                type.setName(neew);
                if(CommandHelper.query("editType", "type", type)){
                    this.loadTableData();
                }
            }
        });

        newType.focusedProperty().addListener((v, old, neew) -> {
            if(neew){
                newType.setStyle("");
            }
        });

        add.setOnAction((e) -> this.addButtonHandler());
        delete.setOnAction((e) -> this.deleteButtonHandler());
    }

    @Override
    public void update() {
        this.loadTableData();
        GUIHelper.resetText(newType);
    }

    @Override
    public Node getNode() {
        return layout;
    }

    private void loadTableData(){
        table.setItems(FXCollections.observableArrayList(CommandHelper.loadListData("allTypes", "types")));
    }

    private void addButtonHandler() {
        if(!Checker.checkString(newType)){
            return;
        }
        ResourceBundle rsBnd = ResourceBundle.getBundle("server");
        try(Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request request = new Request();
            request.setCommand("addType");

            Type type = new Type();
            type.setName(newType.getText());

            request.setAttribute("type", type);

            out.writeObject(request);
            Response response = (Response) in.readObject();

            if(!response.isError()){
                GUIHelper.resetText(newType);
                this.loadTableData();
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void deleteButtonHandler() {
        Type type = table.getSelectionModel().getSelectedItem();
        if(type == null){
            return;
        }
        if(CommandHelper.query("deleteType", "type", type)){
            this.loadTableData();
        } else {
            AlertBox.display("Ошибка", "Данный тип используется в описании продуктов, поэтому его нельзя удалить");
        }
    }
}
