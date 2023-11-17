package com.example.bomberman.control;

import com.example.bomberman.model.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private static final int MAX_MAPS = 3;
    private ArrayList<Map> maps = new ArrayList<>();

    @FXML
    private Canvas canvas;

    @FXML
    private GraphicsContext gc;

    @FXML
    private AnchorPane root;

    public void initialize() {
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gc = canvas.getGraphicsContext2D();
        for(int i = 0; i < MAX_MAPS; i++){
            maps.add(new Map(canvas, 15, 15));
        }

        maps.get(0).paint();

        new Thread(() -> {
            while (true) {;
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                maps.get(0).paint();
            }
        }).start();
    }

    public void paint(){

    }
}