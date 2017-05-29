package com.coursework.controller;

import com.coursework.Updateable;
import com.coursework.bean.Product;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.bean.Type;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ProductController implements Initializable, Updateable {
    @FXML private VBox layout;
    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, Integer> idCol;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, String> infoCol;
    @FXML private TableColumn<Product, String> countCol;
    @FXML private TableColumn<Product, String> priceCol;
    @FXML private TableColumn<Product, String> dateCol;
    @FXML private TableColumn<Product, String> typeCol;
    @FXML private TextField newName;
    @FXML private TextField newInfo;
    @FXML private TextField newCount;
    @FXML private TextField newPrice;
    @FXML private ChoiceBox<String> newType;
    @FXML private Button add;
    @FXML private Button delete;
    @FXML private ChoiceBox<String> choice;
    @FXML private TextField search; //TODO обработку нажатия enter
    @FXML private Button find;
    @FXML private Button reset;
    @FXML private Button report;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FileChooser fileChooser = new FileChooser();
    private File[] file = {null};

    @Override
    public void initialize(URL location, ResourceBundle resources){
        table.setEditable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            if(!Checker.validateString(neew)){
                AlertBox.display("Ошибка", "Недопустимое имя");
                TableView<Product> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            } else {
                Product product = value.getTableView().getSelectionModel().getSelectedItem();
                product.setName(neew);
                if(CommandHelper.query("editProduct", "product", product)){
                   this.loadTableData();
                }
            }
        });
        infoCol.setCellValueFactory(new PropertyValueFactory<>("info"));
        infoCol.setCellFactory(TextFieldTableCell.forTableColumn());
        infoCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            if(!Checker.validateString(neew)){
                AlertBox.display("Ошибка", "Недопустимое описание");
                TableView<Product> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            } else {
                Product product = value.getTableView().getSelectionModel().getSelectedItem();
                product.setInfo(neew);
                if(CommandHelper.query("editProduct", "product", product)){
                    this.loadTableData();
                }
            }
        });
        countCol.setCellValueFactory((value) -> new SimpleObjectProperty<>(String.valueOf(value.getValue().getCount())));
        countCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            if(!Checker.validateInt(neew)){
                AlertBox.display("Ошибка", "Недопустимое количество");
                TableView<Product> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            } else {
                Product product = value.getTableView().getSelectionModel().getSelectedItem();
                product.setCount(Integer.parseInt(neew));
                if(CommandHelper.query("editProduct", "product", product)){
                    this.loadTableData();
                }
            }
        });
        priceCol.setCellValueFactory((value) -> new SimpleObjectProperty<>(String.valueOf(value.getValue().getPrice())));
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        priceCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            if(!Checker.validateInt(neew)){
                AlertBox.display("Ошибка", "Недопустимая цена");
                TableView<Product> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            } else {
                Product product = value.getTableView().getSelectionModel().getSelectedItem();
                product.setPrice(Integer.parseInt(neew));
                if(CommandHelper.query("editProduct", "product", product)){
                    this.loadTableData();
                }
            }
        });
        dateCol.setCellValueFactory((value) -> new SimpleObjectProperty<>(df.format(value.getValue().getDateCreate())));
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setOnEditCommit((value) -> {
            String neew = value.getNewValue();
            try {
                Date date = df.parse(neew);
                Product product = value.getTableView().getSelectionModel().getSelectedItem();
                product.setDateCreate(date);
                if(CommandHelper.query("editProduct", "product", product)){
                    this.loadTableData();
                }
            } catch(ParseException e){
                AlertBox.display("Ошибка", "Неверный формат даты");
                TableView<Product> tableView = value.getTableView();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(),
                        tableView.getSelectionModel().getSelectedItem());
            }
        });
        typeCol.setCellValueFactory((row) -> new SimpleObjectProperty<>(row.getValue().getType().getName()));
        typeCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(this.loadTypes()));
        typeCol.setOnEditCommit((value) -> {
            Product product = value.getTableView().getSelectionModel().getSelectedItem();
            product.getType().setName(value.getNewValue());
            if(CommandHelper.query("editProduct", "product", product)){
                this.loadTableData();
            }
        });

        newName.focusedProperty().addListener((v, old, neew) -> {
            if(neew){
                newName.setStyle("");
            }
        });
        newInfo.focusedProperty().addListener((v, old, neew) -> {
            if(neew){
                newInfo.setStyle("");
            }
        });
        newCount.focusedProperty().addListener((v, old, neew) -> {
            if(neew){
                newCount.setStyle("");
            }
        });
        newPrice.focusedProperty().addListener((v, old, neew) -> {
            if(neew){
                newPrice.setStyle("");
            }
        });

        choice.getItems().addAll("Названию", "Цене", "Типу");
        choice.getSelectionModel().selectFirst();

        add.setOnAction((e) -> this.addButtonHandler());
        delete.setOnAction((e) -> this.deleteButtonHandler());
        find.setOnAction((e) -> this.searchHandler());
        reset.setOnAction((e) -> {
            this.loadTableData();
            search.setText("");
        });
        fileChooser.setTitle("Выберете файл для сохранения отчёта");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("txt", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
        fileChooser.setInitialFileName("Отчёт по продукции");
        report.setOnAction((e) -> {
            file[0] = fileChooser.showSaveDialog(null);
            if(file[0] != null){
                try(PrintWriter pw = new PrintWriter(file[0]);) {
                    StringBuilder sb = new StringBuilder();
                    for(Product product : table.getItems()){
                        sb.append(product).append("\n");
                    }
                    sb.substring(0, sb.length() - 2);

                    pw.print(sb.toString());
                } catch(FileNotFoundException exc){
                    exc.printStackTrace();
                }
                file[0] = null;
            }
        });
    }

    @Override
    public void update() {
        GUIHelper.resetText(newName, newCount, newPrice, newInfo, search);
        newType.setItems(this.loadTypes());
        newType.getSelectionModel().selectFirst();
        this.loadTableData();
    }

    @Override
    public Node getNode() {
        return layout;
    }

    private ObservableList<String> loadTypes(){
        ObservableList<String> list = FXCollections.observableArrayList();
        List<Type> types = CommandHelper.loadListData("allTypes", "types");
        for(Type type : types){
            list.add(type.getName());
        }
        return list;
    }

    private void loadTableData(){
        table.setItems(FXCollections.observableArrayList(CommandHelper.loadListData("allProducts", "products")));
    }

    private void addButtonHandler(){
        if(!this.checkValues()){
            return;
        }
        ResourceBundle rsBnd = ResourceBundle.getBundle("server");
        try(Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request request = new Request();
            request.setCommand("addProduct");

            Product product = new Product();
            product.setName(newName.getText());
            product.setInfo(newInfo.getText());
            product.setCount(Integer.parseInt(newCount.getText()));
            product.setPrice(Integer.parseInt(newPrice.getText()));
            product.setDateCreate(new Date());
            Type type = new Type();
            type.setName(newType.getSelectionModel().getSelectedItem());
            product.setType(type);

            request.setAttribute("product", product);

            out.writeObject(request);
            Response response = (Response) in.readObject();

            if(!response.isError()){
                GUIHelper.resetText(newName, newCount, newInfo, newPrice);
                this.loadTableData();
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void deleteButtonHandler() {
        Product product = table.getSelectionModel().getSelectedItem();
        if(product != null){
            if(CommandHelper.query("deleteProduct", "product", product)){
                this.loadTableData();
            } else {
                //error occurred
            }
        }
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

    private boolean checkValues(){
        return Checker.checkString(newName) && Checker.checkString(newInfo)
                && Checker.checkInt(newCount) && Checker.checkInt(newPrice);
    }

}
