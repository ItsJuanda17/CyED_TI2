package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Objects;

public class Enemy extends Character implements IExploitable{

    public Enemy(Canvas canvas, Map map) {
        super(canvas, map);
        initEnemy();
    }

    private void initEnemy() {
        this.width = 32;
        this.height = 32;
        this.speed = 1;
        this.lives = 1;
        this.state = CharacterState.IDLE;
        this.frame = 0;
        this.position = new Vector(10, 10);
        this.idle.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/idle/enemy-idle-00.png"))));

        for(int i = 0; i < 2; i++) {
            this.runUp.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/run/enemy-run-up-0" + i + ".png"))));
        }

        for(int i = 0; i < 1; i++) {
            this.runDown.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/run/enemy-run-down-0" + i + ".png"))));
        }

        for (int i = 0; i < 1; i++) {
            this.runLeft.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/run/enemy-run-left-0" + i + ".png"))));
        }

        for (int i = 0; i < 1; i++) {
            this.runRight.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/run/enemy-run-right-0" + i + ".png"))));
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void die() {

    }

    @Override
    public void paint() {
        gc.drawImage(idle.get(0), position.getPosX(), position.getPosY());
    }
}
