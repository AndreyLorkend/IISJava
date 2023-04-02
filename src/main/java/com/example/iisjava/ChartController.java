package com.example.iisjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChartController implements Initializable {
    private ProfileDrawer drawer;
    @FXML
    private Button drawGraphBtn;
    @FXML
    private Button drawProfileBtn;
    @FXML
    private Spinner<Double> zNumberInput;
    @FXML
    private Spinner<Double> uNumberInput;
    @FXML
    private Canvas graph;
    @FXML
    void onDrawProfile(ActionEvent event) {
        this.drawer.setParams(this.zNumberInput.getValue(), this.uNumberInput.getValue());
        Thread drawThread = new Thread(this.drawer);
        drawThread.start();
    }
    @FXML
    void onDrawGraph(ActionEvent event) {
        this.graph.getGraphicsContext2D().clearRect(0, 0, graph.getWidth(), graph.getHeight());
        this.drawer.drawResearchArea();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.drawer = new ProfileDrawer(this.graph);
        this.drawer.drawResearchArea();
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(5.0, 20.0);
        SpinnerValueFactory<Double> uValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.001, 1);
        uValueFactory.setValue(0.01);
        valueFactory.setValue(5.0);
        zNumberInput.setValueFactory(valueFactory);
        uNumberInput.setValueFactory(uValueFactory);
    }
}