package com.example.bomberman.control;

import com.example.bomberman.model.Map;
import com.example.bomberman.model.RandomMapGenerator;
import com.example.bomberman.model.Tile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    public static final int MAX_MAPS = 3;
    private ArrayList<Map> maps = new ArrayList<>();

    @FXML
    private Canvas canvas;

    @FXML
    private GraphicsContext gc;

    @FXML
    private AnchorPane root;

    private Map currentMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gc = canvas.getGraphicsContext2D();
        this.currentMap = new Map(this.canvas, 45, 13);
        /*
        this.randomMapGenerator = new RandomMapGenerator(45,13);
        Tile[][] grid = randomMapGenerator.getGrid();
        for (Tile[] tiles : grid) {
            for (Tile tile : tiles) {
                tile.setCanvas(canvas);
                tile.setGc(gc);
            }
        }
         */
        canvas.toFront();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvas.widthProperty().addListener((observable, oldValue, newValue) -> repaintMaze());
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> repaintMaze());
        repaintMaze();
    }

    private void repaintMaze() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        paint();
    }
    public void paint(){
        currentMap.paint();
    }
}