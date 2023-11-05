package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.ArrayList;

public abstract class Character extends GameEntity{

    protected int speed;
    protected int lives;
    protected int frame;
    protected CharacterState state;
    protected ArrayList<Image> idle;
    protected ArrayList<Image> run;
    protected ArrayList<Image> death;

    public Character(Canvas canvas) {
        super(canvas);
        this.idle = new ArrayList<>();
        this.death = new ArrayList<>();
    }

    public int getSpeed() {
        return speed;
    }

    public int getLives() {
        return lives;
    }

    public CharacterState getState() {
        return state;
    }

    public abstract void move();

    public abstract void die();

}
