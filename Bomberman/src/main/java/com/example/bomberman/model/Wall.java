package com.example.bomberman.model;
import javafx.scene.canvas.Canvas;
public class Wall extends GameEntity{

    private boolean destroyed;

    public Wall(Canvas canvas) {
        super(canvas);
        this.destroyed = false;

    }

    @Override
    public void paint() {

    }
}
