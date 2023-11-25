package com.example.bomberman.model;

import com.example.bomberman.control.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile extends GameEntity{

    public static final int TILE_WIDTH = 25;
    public static final int TILE_HEIGHT = 25;
    private GameEntity content;
    private TileState state;

    public Tile(Canvas canvas, int x, int y) {
        super(canvas);
        this.position = new Vector(x, y);
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        updateHitBox();
    }

    public GameEntity getContent() {
        return this.content;
    }

    public void setContent(GameEntity content) {
        this.content = content;
    }

    public TileState getState() {
        return this.state;
    }

    public void setState(TileState state) {
        this.state = state;
        this.content = state == TileState.PASSAGE ? null : state == TileState.BREAKABLE_WALL ? new DestructibleWall(canvas, position.getPosX(), position.getPosY()) : new Wall(canvas, position.getPosX(), position.getPosY());
    }

    @Override
    public void onCollision(GameEntity other) {
        if(content != null) content.onCollision(other);
    }

    @Override
    public void paint(){
        if(content != null) {
            content.paint();
        }else {
            gc.setFill(Color.GREEN);
            gc.fillRect(position.getPosX() * TILE_WIDTH, position.getPosY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        }
        /*
        gc.setFill(state == TileState.PASSAGE ? Color.GREEN : state == TileState.BREAKABLE_WALL ? Color.GRAY : Color.BLACK);
        gc.fillRect(position.getPosX() * TILE_WIDTH, position.getPosY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
         */
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Tile) {
            Tile tile = (Tile) obj;
            return this.position.equals(tile.getPosition());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "position=" + position +
                ", content=" + content +
                ", state=" + state +
          '}';
    }

}