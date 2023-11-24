package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Explosion extends GameEntity {

    private static final int EXPLOSION_DURATION = 500;
    private static final int NUM_FRAMES = 6;

    private Timer explosionTimer;
    private int currentFrame;

    private Image[] images;

    private boolean exploded;

    private Map map;

    public Explosion(Canvas canvas, Vector position) {
        super(canvas);
        initializeExplosion(position);
        this.exploded = false;
    }

    private void initializeExplosion(Vector position) {
        this.width = 25;
        this.height = 25;
        this.currentFrame = 0;
        this.position = position;
        this.images = new Image[NUM_FRAMES];

        for (int i = 0; i < NUM_FRAMES; i++) {
            this.images[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Bomb/explosion/explosion-00.png")), width, height, false, false);
        }
    }

    public void startExplosion() {
        setPosition(position);
        startExplosionTimer();
        startAnimationTimer();
    }

    private void startExplosionTimer() {
        explosionTimer = new Timer();
        explosionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                finishExplosion();
            }
        }, EXPLOSION_DURATION);
    }

    private void finishExplosion() {
        exploded = true;
        explosionTimer.cancel();
        explosionTimer.purge();
    }

    private void startAnimationTimer() {
        Timer animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                animate();
            }
        }, 0, 100);
    }

    private void animate() {
        if (!exploded) {
            currentFrame = (currentFrame + 1) % NUM_FRAMES;
            paint();
        }

        if (exploded && currentFrame == NUM_FRAMES - 1) {
            finishExplosion();
        }
    }

    @Override
    public void onCollision(GameEntity other) {
        if(other instanceof Player || other instanceof Enemy) {
            other.onCollision(this);
        }
    }

    @Override
    public void paint() {
        updateHitBox();
        paintHitBox();
        gc.drawImage(images[currentFrame], position.getPosX() * Tile.TILE_WIDTH, position.getPosY() * Tile.TILE_HEIGHT);
        }

}

