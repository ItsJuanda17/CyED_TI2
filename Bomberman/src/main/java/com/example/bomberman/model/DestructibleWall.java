package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;

import javafx.scene.paint.Color;

public class DestructibleWall extends GameEntity {

    private boolean destroyed;
    private TileState state;

    public DestructibleWall(Canvas canvas) {
        super(canvas);
        this.destroyed = false;
        this.state = TileState.BREAKABLE_WALL;

    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        this.destroyed = true;
    }


  
//paint beta
  
    @Override
    public void paint() {
        gc.setFill(getFillColor());
        gc.fillRect(position.getPosX() * 10, position.getPosY() * 10, 10, 10);
    }

    private Color getFillColor() {
        return state == TileState.BREAKABLE_WALL ? Color.GRAY : Color.BLACK;
    }
}
