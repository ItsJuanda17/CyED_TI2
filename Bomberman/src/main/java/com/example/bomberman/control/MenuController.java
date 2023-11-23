package com.example.bomberman.control;

import com.example.bomberman.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable{

    @FXML
    private Button button;

    @FXML
    private Label label;

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;

    private Image title;

    @FXML
    private void startGame() {
        try {
            FXMLLoader gameLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene gameScene = new Scene(gameLoader.load(), 1200, 600);
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setResizable(false);
            stage.setScene(gameScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gc = canvas.getGraphicsContext2D();
        this.title = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/screens/mainMenu-01.png")), 550, 275, false, false);

        this.button.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/game_font.ttf"), 20));
        this.button.setTextFill(Color.WHITE);
        this.button.setOnMouseEntered(event -> button.setTextFill(Color.YELLOW));
        this.button.setOnMouseExited(event -> button.setTextFill(Color.WHITE));
        this.button.setStyle("-fx-background-color: transparent");

        this.label.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/game_font.ttf"), 5));
        this.label.setTextFill(Color.WHITE);


        gc.fillRect(0, 0, 600, 400);
        gc.setFill(Color.BLACK);
        double imageX = (canvas.getWidth() - title.getWidth()) / 2;
        double imageY = 10;

        gc.drawImage(title, imageX, imageY);
    }
}
