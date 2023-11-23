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

    public Explosion(Canvas canvas , Vector position) {
        super(canvas);
        initializeExplosion();
        this.position = position;
    }

    private void initializeExplosion() {
        this.width = 25;
        this.height = 25;
        this.currentFrame = 0;
        this.position = new Vector(0, 0);
        this.images = new Image[NUM_FRAMES];

        for (int i = 0; i < NUM_FRAMES; i++) {
            this.images[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Bomb/explosion/explosion-00.png")), width, height, false, false);
        }
    }

    public void startExplosion(Vector position) {
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
        }, 0, 100);  // Ajusta la velocidad de la animación según sea necesario
    }

    private void animate() {
        currentFrame = (currentFrame + 1) % NUM_FRAMES;
        paint();
    }

    @Override
    public void paint() {
        gc.drawImage(images[currentFrame], position.getPosX(), position.getPosY());
    }
}
