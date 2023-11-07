package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Bomb extends GameEntity{

    private int range;
    private int timer;
    private boolean exploded;
    private ArrayList<Image> bombImages;
    private ArrayList<Image> explosionImages;

    public Bomb(Canvas canvas, int timer, int range) {
        super(canvas);
        this.timer = timer;
        this.range = range;
        this.exploded = false;
    }

    public int getTimer() {
        return this.timer;
    }

    public int getRange() {
        return this.range;
    }

    public boolean isExploded() {
        return this.exploded;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public void decrementTimer() {
        this.timer--;
    }

    public void incrementRange() {
        this.range++;
    }

    public void decrementRange() {
        this.range--;
    }

    public void explode() {
        this.exploded = true;
    }

    public void resetTimer() {
        this.timer = 3;
    }

    public void resetRange() {
        this.range = 1;
    }

    public void resetExploded() {
        this.exploded = false;
    }

    public void reset() {
        this.resetTimer();
        this.resetRange();
        this.resetExploded();
    }

    @Override
    public void paint(){
    }
}