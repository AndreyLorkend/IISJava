package com.example.iisjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ChartController {
    private final int SEED = 65539;
    private final double MAX_RANDOM = 32767.0;
    private RandomGenerator randomGenerator = new RandomGenerator(SEED);
    @FXML
    private Button drawProfileBtn;

    @FXML
    private TextField zNumberInput;

    @FXML
    void onDrawProfile(ActionEvent event) {
        System.out.println((double)this.randomGenerator.next() / MAX_RANDOM);
    }
}