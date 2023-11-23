package com.example.bomberman.control;

import com.example.bomberman.HelloApplication;
import com.example.bomberman.model.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageRange;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


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
    private int currentMapIndex;
    private Map currentMap;
    private Clip bombClip;
    private boolean win;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gameGc = gameCanvas.getGraphicsContext2D();
        this.statsGc = statsCanvas.getGraphicsContext2D();
        initMaps();
        initActions();
        this.currentMap = maps.get(currentMapIndex);

        try {
            String soundPath = "/Sounds/soundtrack/06_Bomberman-Hero-Cell.wav";
            InputStream inputStream = getClass().getResourceAsStream(soundPath);

            if (inputStream != null) {
                // Almacenar el contenido en un búfer
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();


                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.toByteArray());
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);


                if (audioInputStream.markSupported()) {
                    audioInputStream.reset();
                }

                bombClip = AudioSystem.getClip();
                bombClip.open(audioInputStream);
                bombClip.start();
            } else {
                System.err.println("No se pudo encontrar el recurso: " + soundPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    mainGame();
                });
                try {
                    Thread.sleep(75);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void mainGame(){
        paint();

        if(currentMap.getPlayer().getLives() == 0) {
            win = false;
            finishGame();
        }

        if(currentMap.getEnemies().isEmpty() && currentMap.getPlayer().getPosition().equals(currentMap.getExitPoint().getPosition())){
            loadNextMap();
        }
    }


    public void initMaps(){
        for(int i = 0; i < MAX_MAPS; i++){
            maps.add(new Map(gameCanvas));
        }
        this.currentMapIndex = 0;
    }

    public void initActions(){
        gameCanvas.setOnKeyReleased(event ->{
            currentMap.setOnKeyReleased(event);

        });

        gameCanvas.setOnKeyPressed(event ->{
            currentMap.setOnKeyPressed(event);
        });
    }

    private void loadNextMap() {
        if(currentMapIndex == MAX_MAPS - 1){
            win = true;
            finishGame();
        }else {
            currentMapIndex++;
            currentMap = maps.get(currentMapIndex);
        }
    }

    private void finishGame() {
        gameOverScreen();
    }

    private void gameOverScreen() {
        try {
            FXMLLoader gameOverLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/bomberman/game-over-view.fxml"));
            Scene gameOverScene = new Scene(gameOverLoader.load(), 600, 400);
            GameOverController gameOverController = gameOverLoader.getController();
            gameOverController.setWin(win);
            Stage stage = (Stage) gameCanvas.getScene().getWindow();
            stage.setScene(gameOverScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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