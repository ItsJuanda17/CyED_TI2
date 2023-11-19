package com.example.bomberman.control;

import com.example.bomberman.model.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    public static final double GAME_WIDTH = 1200;
    public static final double GAME_HEIGHT = 500;
    public static final int MAX_MAPS = 3;
    private ArrayList<Map> maps = new ArrayList<>();

    @FXML
    private Canvas gameCanvas;
    private GraphicsContext gameGc;

    @FXML
    private Canvas statsCanvas;
    private GraphicsContext statsGc;

    @FXML
    private AnchorPane root;

    private Map currentMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gameGc = gameCanvas.getGraphicsContext2D();
        this.statsGc = statsCanvas.getGraphicsContext2D();

        this.currentMap = new Map(this.gameCanvas);

        initActions();

        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    paint();
                });
                try {
                    Thread.sleep(75);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void initActions(){
        gameCanvas.setOnKeyReleased(event ->{
            currentMap.setOnKeyReleased(event);
        });

        gameCanvas.setOnKeyPressed(event ->{
            currentMap.setOnKeyPressed(event);
        });
    }

    public void paint(){
        paintStatsPanel();
        currentMap.paint();
    }

    private void paintStatsPanel(){
        int iconSize = 50;
        Image heartRedIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/statsPanel/heart-red.png")), iconSize, iconSize, false, false);
        Image bombIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/statsPanel/icon-bomb.png")), iconSize, iconSize, false, false);
        Image enemyIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/statsPanel/icon-enemy.png")), iconSize, iconSize, false, false);

        // Establecer el color de fondo en gris
        statsGc.setFill(Color.ORANGE);
        statsGc.fillRect(0, 0, statsCanvas.getWidth(), statsCanvas.getHeight());

        int hearthDistance = 65;
        for(int i = 0; i < currentMap.getPlayer().getLives(); i++){
            statsGc.drawImage(heartRedIcon, 970 + (i*hearthDistance), 25);
        }

        statsGc.drawImage(bombIcon, 810, 25);
        statsGc.setFill(Color.BLACK);
        statsGc.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 50));
        statsGc.fillText(""+currentMap.getPlayer().getBombs().size(), 860, 75);

        statsGc.drawImage(enemyIcon, 650, 25);
        statsGc.setFill(Color.BLACK);
        statsGc.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 50));
        statsGc.fillText(""+currentMap.getEnemies().size(), 700, 75);
    }



}