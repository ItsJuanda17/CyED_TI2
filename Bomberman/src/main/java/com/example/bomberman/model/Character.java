package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.ArrayList;

public abstract class Character extends GameEntity{

    public enum CharacterState {
        IDLE,
        RUN_UP,
        RUN_DOWN,
        RUN_LEFT,
        RUN_RIGHT
    }

    protected int speed;
    protected int lives;
    protected int frame;
    protected CharacterState state;
    protected ArrayList<Image> idle;
    protected ArrayList<Image> runUp;
    protected ArrayList<Image> runDown;
    protected ArrayList<Image> runLeft;
    protected ArrayList<Image> runRight;
    protected ArrayList<Image> death;
    protected Map map;

    public Character(Canvas canvas, Map map) {
        super(canvas);
        this.idle = new ArrayList<>();
        this.runUp = new ArrayList<>();
        this.runDown = new ArrayList<>();
        this.runLeft = new ArrayList<>();
        this.runRight = new ArrayList<>();
        this.death = new ArrayList<>();
    }

    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLives() {
        return lives;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }

    public CharacterState getState() {
        return state;
    }
    public void setState(CharacterState state) {
        this.state = state;
    }

    public Map getMap() {
        return map;
    }
    public void setMap(Map map) {
        this.map = map;
    }

    public abstract void move();

    public abstract void die();

}
