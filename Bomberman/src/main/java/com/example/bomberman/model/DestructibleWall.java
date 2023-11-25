package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.*;

public class DestructibleWall extends Wall {

    private int currentFrame;
    private List<Image> explosion;


    public DestructibleWall(Canvas canvas, int x, int y) {
        super(canvas, x, y);
        this.explosion = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            explosion.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/walls/destructibleWall-explosion-0" + i + ".png")), Tile.TILE_WIDTH, Tile.TILE_HEIGHT, false, false));
        }
    }

    @Override
    public void placeImage() {
        idle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/walls/destructibleWall-idle-00.png")), Tile.TILE_WIDTH, Tile.TILE_HEIGHT, false, false);
    }

    public void destroy() {
        this.destroyed = true;
        startExplosionAnimation();
    }

    private void startExplosionAnimation(){
        Timer explosionTimer = new Timer();
        explosionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                while(currentFrame < explosion.size()) {
                    animateExplosion();
                    explosionTimer.cancel();
                }
            }
        }, Explosion.EXPLOSION_DURATION);
    }

    public void animateExplosion() {
        gc.drawImage(explosion.get(currentFrame % explosion.size()), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
        currentFrame++;
    }

}
