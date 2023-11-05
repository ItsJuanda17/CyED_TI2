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
}
