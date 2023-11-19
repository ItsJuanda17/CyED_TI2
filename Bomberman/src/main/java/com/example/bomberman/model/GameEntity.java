package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameEntity {

    protected Canvas canvas;
    protected GraphicsContext gc;
    protected int width;
    protected int height;
    protected Vector position;

    public GameEntity(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector getPosition() {
        return position;
    }

    public int getX() {
        return position.getPosX();
    }

    public int getY() {
        return position.getPosY();
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position = new Vector(x, y);
    }

    public abstract void paint();
}
