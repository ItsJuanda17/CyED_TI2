package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;

import java.util.ArrayList;

public class Player extends Character implements IExploitable{

    private ArrayList<Bomb> bombs;

    public Player(Canvas canvas) {
        super(canvas);
        this.bombs = new ArrayList<>();
    }

    @Override
    public void move() {

    }

    @Override
    public void die() {

    }

    @Override
    public void paint() {

    }

    public void incrementBombCount() {
        this.bombs.add(new Bomb(this.canvas, 3, 1));
    }

    public void incrementBombRange() {
        this.bombs.forEach(Bomb::incrementRange);
    }

    public void incrementSpeed() {
        this.speed++;
    }
}
