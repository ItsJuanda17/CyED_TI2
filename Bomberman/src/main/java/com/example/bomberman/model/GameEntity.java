package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class GameEntity{

    protected Canvas canvas;
    protected GraphicsContext gc;
    protected int width;
    protected int height;
    protected Vector position;
    protected Rectangle hitBox;

    public GameEntity(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.hitBox = new Rectangle();
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

    public Rectangle getHitBox() {
        return hitBox;
    }

    protected void paintHitBox() {
        gc.setStroke(Color.RED);
        gc.strokeRect(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
    }

    public void updateHitBox() {
        hitBox.setX(position.getPosX() * Tile.TILE_WIDTH);
        hitBox.setY(position.getPosY() * Tile.TILE_HEIGHT);
        hitBox.setWidth(width);
        hitBox.setHeight(height);
    }

    public boolean intersect(GameEntity other){
        return this.hitBox.intersects(other.hitBox.getLayoutBounds());
    }

    public abstract void onCollision(GameEntity other);

    public abstract void paint();

}