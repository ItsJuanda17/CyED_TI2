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

    private ArrayList<Explosion> explosions;

    private Vector playerPosition;

    private int timer;
    private int currentFrame;

    private Map map;

    public Bomb(Canvas canvas , Map map , int playerX , int playerY) {
        super(canvas);
        this.map = map;
        this.explosions = new ArrayList<>();
        initializeBomb(playerX, playerY);
    }

    private void initializeBomb(int playerX, int playerY) {
        this.width = 25;
        this.height = 25;
        this.timer = 3;
        this.fireRange = 1;
        this.exploded = false;
        this.currentFrame = 0;
        this.idle = new ArrayList<>();
        this.position = new Vector(playerX, playerY);

        for (int i = 0; i < IDLE_FRAMES; i++) {

            this.idle.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Bomb/idle/idle-bomb-0" + i + ".png")), width, height, false, false));
        }
    }

    public void dropBomb(int timer, int fireRange, int playerX, int playerY) {
        this.position = new Vector(playerX, playerY);
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
        explosions.add(new Explosion(canvas, position));
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
        gc.drawImage(idle.get(currentFrame), position.getPosX() * Tile.TILE_WIDTH, position.getPosY() * Tile.TILE_HEIGHT);
    }

}
