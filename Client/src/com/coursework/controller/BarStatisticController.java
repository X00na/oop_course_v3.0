package com.coursework.controller;

import com.coursework.Updateable;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

public class BarStatisticController implements Initializable, Updateable {
    @FXML private VBox layout;
    @FXML private BarChart<String, Number> chart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xAxis.setLabel("Товар");
        yAxis.setLabel("Количество заказов");

        this.loadData();
    }

    @Override
    public void update() {
        this.loadData();
    }

    @Override
    public Node getNode() {
        return layout;
    }

    private void loadData() {
        ResourceBundle rsBnd = ResourceBundle.getBundle("server");
        try(Socket socket = new Socket("localhost", Integer.parseInt(rsBnd.getString("port")));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request request = new Request();
            request.setCommand("statistic");

            out.writeObject(request);
            Response response = (Response) in.readObject();

            if(!response.isError()){
                Map<String, Integer> map = (Map<String, Integer>) response.getParameter("statistics");
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                for(Map.Entry<String, Integer> entry : map.entrySet()){
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                }
                chart.setData(FXCollections.observableArrayList(Collections.singletonList(series)));
                chart.setLegendVisible(false);
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
