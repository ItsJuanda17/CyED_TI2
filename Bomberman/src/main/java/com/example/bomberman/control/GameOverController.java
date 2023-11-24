package com.example.bomberman.control;

import com.example.bomberman.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    @FXML
    private HBox hBox;

    @FXML
    private Button continueButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label label;

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;

    private boolean win;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gc = canvas.getGraphicsContext2D();

        this.continueButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/game_font.ttf"), 10));
        this.continueButton.setTextFill(Color.WHITE);
        this.continueButton.setOnMouseEntered(event -> continueButton.setTextFill(Color.YELLOW));
        this.continueButton.setOnMouseExited(event -> continueButton.setTextFill(Color.WHITE));
        this.continueButton.setStyle("-fx-background-color: transparent;");

        this.exitButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/game_font.ttf"), 10));
        this.exitButton.setTextFill(Color.WHITE);
        this.exitButton.setOnMouseEntered(event -> exitButton.setTextFill(Color.YELLOW));
        this.exitButton.setOnMouseExited(event -> exitButton.setTextFill(Color.WHITE));
        this.exitButton.setStyle("-fx-background-color: transparent;");

        if(win) {
            this.label.setText("YOU WON!");
        } else {
            this.label.setText("GAME OVER");
        }

        this.label.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/game_font.ttf"), 20));
        this.label.setTextFill(Color.WHITE);

        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        hBox.setStyle("-fx-background-color: transparent;");

        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public void restart() {
        try {
            FXMLLoader gameLoader = new FXMLLoader(GameOverController.class.getResource("/com/example/bomberman/hello-view.fxml"));
            Scene gameScene = new Scene(gameLoader.load(), 1200, 600);
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setResizable(false);
            stage.setScene(gameScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        System.exit(0);
}
}