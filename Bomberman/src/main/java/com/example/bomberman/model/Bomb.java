package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends GameEntity {

    private static final int EXPLOSION_TIME = 3000;
    private static final int MAX_FIRE_RANGE = 5;
    private static final int IDLE_FRAMES = 2;

    private int fireRange;
    private boolean exploded;
    private Timer explosionTimer;
    private ArrayList<Image> idle;

    private int timer;
    private int currentFrame;

    public Bomb(Canvas canvas) {
        super(canvas);
        initializeBomb();
    }

    private void initializeBomb() {
        this.width = 25;
        this.height = 25;
        this.timer = 3;
        this.fireRange = 1;
        this.exploded = false;
        this.currentFrame = 0;
        this.position = new Vector(0, 0);
        this.idle = new ArrayList<>();

        for (int i = 0; i < IDLE_FRAMES; i++) {

            this.idle.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Bomb/idle/idle-bomb-0" + i + ".png")), width, height, false, false));
        }
    }

    public void dropBomb(Vector position, int timer, int fireRange) {
        setPosition(position);
        this.timer = timer;
        this.fireRange = Math.min(fireRange, MAX_FIRE_RANGE);
        startExplosionTimer();
        startAnimationTimer();
    }

    private void startExplosionTimer() {
        explosionTimer = new Timer();
        explosionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                explode();
            }
        }, EXPLOSION_TIME);
    }

    private void explode() {
        exploded = true;
    }

    private void startAnimationTimer() {
        Timer animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                animate();
            }
        }, 0, 500);
    }

    private void animate() {
        if (!exploded) {
            currentFrame = (currentFrame + 1) % IDLE_FRAMES;
            paint();
        }
    }

    public int getFireRange() {
        return fireRange;
    }

    public void updateFireRange(int newFireRange) {
        this.fireRange = Math.min(newFireRange, MAX_FIRE_RANGE);
    }

    @Override
    public void paint() {
        gc.drawImage(idle.get(currentFrame), position.getPosX(), position.getPosY());
    }
}
