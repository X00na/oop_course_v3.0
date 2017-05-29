package com.coursework.controller;

import com.coursework.Updateable;
import com.coursework.bean.Order;
import com.coursework.bean.Product;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.util.AlertBox;
import com.coursework.util.Checker;
import com.coursework.util.CommandHelper;
import com.coursework.util.GUIHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable, Updateable {
    @FXML private VBox layout;
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, Integer> idCol;
    @FXML private TableColumn<Order, String> countCol;
    @FXML private TableColumn<Order, String> dateCol;
    @FXML private TableColumn<Order, String> infoCol;
    @FXML private TableColumn<Order, String> productCol;
    @FXML private TextField newCount;
    @FXML private TextField newInfo;
    @FXML private TextField search;
    @FXML private ChoiceBox<String> newProduct;
    @FXML private ChoiceBox<String> choice;
    @FXML private Button add;
    @FXML private Button delete; //TODO протестируй функционал
    @FXML private Button find;
    @FXML private Button reset;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.setEditable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        countCol.setCellValueFactory((value) -> new SimpleObjectProperty<>(String.valueOf(value.getValue().getCount())));
        countCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            if(!Checker.validateInt(neew)){
                AlertBox.display("Ошибка", "Недопустимое количество");
                TableView<Order> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            } else {
                Order order = value.getTableView().getSelectionModel().getSelectedItem();
                order.setCount(Integer.parseInt(neew));
                if(CommandHelper.query("editOrder", "order", order)){
                    this.loadTableData();
                }
            }
        });
        dateCol.setCellValueFactory((value) -> new SimpleObjectProperty<>(df.format(value.getValue().getDateOrder())));
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            try {
                Date date = df.parse(neew);
                Order order = value.getTableView().getSelectionModel().getSelectedItem();
                order.setDateOrder(date);
                if(CommandHelper.query("editOrder", "order", order)){
                    this.loadTableData();
                }
            } catch(ParseException e){
                AlertBox.display("Ошибка", "Неверный формат даты");
                TableView<Order> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            }
        });
        infoCol.setCellValueFactory(new PropertyValueFactory<>("info"));
        infoCol.setCellFactory(TextFieldTableCell.forTableColumn());
        infoCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            if(!Checker.validateString(neew)){
                AlertBox.display("Ошибка", "Недопустимое описание");
                TableView<Order> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            } else {
                Order order = value.getTableView().getSelectionModel().getSelectedItem();
                order.setInfo(neew);
                if(CommandHelper.query("editOrder", "order", order)){
                    this.loadTableData();
                }
            }
        });
        productCol.setCellValueFactory((value) -> new SimpleObjectProperty<>(value.getValue().getProduct().getName()));
        productCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(this.loadProducts()));
        productCol.setOnEditCommit((value) -> {
            Order order = value.getTableView().getSelectionModel().getSelectedItem();
            order.getProduct().setName(value.getNewValue());
            if(CommandHelper.query("editOrder", "order", order)){
                this.loadTableData();
            }
        });

        newCount.focusedProperty().addListener((v, old, neew) -> {
            if(neew){
                newCount.setStyle("");
            }
        });
        newInfo.focusedProperty().addListener((v, old, neew) -> {
            if(neew){
                newInfo.setStyle("");
            }
        });

        choice.getItems().addAll("Дате", "Продукту");
        choice.getSelectionModel().selectFirst();

        add.setOnAction((e) -> this.addButtonHandler());
        delete.setOnAction((e) -> this.deleteButtonHandler());
        find.setOnAction((e) -> this.searchHandler());
        reset.setOnAction((e) -> {
            this.loadTableData();
            search.setText("");
        });
    }

    @Override
    public void update() {
        newProduct.setItems(this.loadProducts());
        newProduct.getSelectionModel().selectFirst();
        GUIHelper.resetText(newCount, newInfo, search);
        this.loadTableData();
    }

    @Override
    public Node getNode() {
        return layout;
    }

    private void loadTableData() {
        table.setItems(FXCollections.observableArrayList(CommandHelper.loadListData("allOrders", "orders")));
    }

    private ObservableList<String> loadProducts(){
        ObservableList<String> list = FXCollections.observableArrayList();
        List<Product> products = CommandHelper.loadListData("allProducts", "products");
        for(Product product : products){
            list.add(product.getName());
        }
        return list;
    }

    private void addButtonHandler() {
        if(!(Checker.checkString(newInfo) && Checker.checkInt(newCount))){
            return;
        }
        ResourceBundle rsBnd = ResourceBundle.getBundle("server");
        try(Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request request = new Request();
            request.setCommand("addOrder");

            Order order = new Order();
            order.setCount(Integer.parseInt(newCount.getText()));
            order.setInfo(newInfo.getText());
            order.setDateOrder(new Date());
            Product product = new Product();
            product.setName(newProduct.getSelectionModel().getSelectedItem());
            order.setProduct(product);

            request.setAttribute("order", order);

            out.writeObject(request);
            Response response = (Response) in.readObject();

            if(!response.isError()){
                GUIHelper.resetText(newCount, newInfo);
                this.loadTableData();
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void deleteButtonHandler() {
        Order order = table.getSelectionModel().getSelectedItem();
        if(order != null){
            if(CommandHelper.query("deleteOrder", "order", order)){
                this.loadTableData();
            } else {
                //error occurred
            }
        }
    }

    private void searchHandler() {
        String searchValue = search.getText();
        if(searchValue.isEmpty()){
            this.loadTableData();
        } else {
            table.setItems(FXCollections.observableArrayList(CommandHelper.search("searchOrder",
                    choice.getSelectionModel().getSelectedItem(), searchValue, "orders")));
        }
    }
}
