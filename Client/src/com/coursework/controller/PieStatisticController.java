package com.coursework.controller;

import com.coursework.Updateable;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

public class PieStatisticController implements Initializable, Updateable {
    @FXML private VBox layout;
    @FXML private PieChart chart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
                for(Map.Entry<String, Integer> entry : map.entrySet()){
                    list.add(new PieChart.Data(entry.getKey() + " - " + entry.getValue() + " шт.", entry.getValue()));
                }
                chart.setData(list);
                chart.setLegendSide(Side.BOTTOM);
                chart.setLabelsVisible(true);
                chart.setStartAngle(90);
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
