package com.example.bomberman.model;

import com.example.bomberman.control.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Tile {

    Canvas canvas;
    GraphicsContext gc;
    private Vector position;
    private GameEntity content;
    private TileState state;

    public Tile(int x, int y) {
        this.position = new Vector(x, y);
        this.state = TileState.PASSAGE;
    }

    public Tile(Vector position) {
        this.position = position;
    }

    public Vector getPosition() {
        return this.position;
    }

    public int getX() {
        return this.position.getPosX();
    }

    public int getY() {
        return this.position.getPosY();
    }

    public GameEntity getContent() {
        return this.content;
    }

    public TileState getState() {
        return this.state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public void setContent(GameEntity content) {
        this.content = content;
    }

    public void paint(Canvas canvas){
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        content.paint();
    }

}

