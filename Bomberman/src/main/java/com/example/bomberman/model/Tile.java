package com.example.bomberman.model;

import com.example.bomberman.control.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile {

    private static final int TILE_SIZE = 10;
    Canvas canvas;
    GraphicsContext gc;
    private Vector position;
    private GameEntity content;
    private TileState state;

    public Tile(int x, int y) {
        this.position = new Vector(x, y);
    }

    public Tile(Vector position) {
        this.position = position;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
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

    public void setPosition(Vector position) {
        this.position = position;
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
        gc.setFill(state == TileState.PASSAGE ? Color.WHITE : state == TileState.BREAKABLE_WALL ? Color.GRAY : Color.BLACK);
        gc.fillRect(position.getPosX() * TILE_SIZE, position.getPosY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile tile = (Tile) obj;
            return this.position.equals(tile.getPosition());
        }
        return false;
    }
}

