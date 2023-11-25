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
    private static final int TIMER = 3;
    private int fireRange;
    private boolean exploded;
    private Timer explosionTimer;
    private ArrayList<Image> idle;
    private ArrayList<Explosion> explosions;
    private Vector playerPosition;
    private int currentFrame;
    private Map map;
    private boolean isPlaced;

    public Bomb(Canvas canvas, Map map) {
        super(canvas);
        this.map = map;
        this.explosions = new ArrayList<>();
        initializeBomb();
    }

    private void initializeBomb() {
        this.width = 25;
        this.height = 25;
        this.fireRange = 1;
        this.exploded = false;
        this.currentFrame = 0;
        this.idle = new ArrayList<>();
        for (int i = 0; i < IDLE_FRAMES; i++) {
            this.idle.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Bomb/idle/idle-bomb-0" + i + ".png")), width, height, false, false));
        }
    }

    public void dropBomb(Vector position) {
        this.position = position;
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
        for(int i = 0; i < fireRange; i++){

            Tile tile;
            if((tile = map.getTile(position)).getState() != TileState.BLOCKED){
                Explosion explosionCenter = new Explosion(canvas, new Vector(position.getPosX(), position.getPosY()), map);
                map.setTileAsPassage(position.getPosX(), position.getPosY());
                map.getTile(explosionCenter.getPosition()).setContent(explosionCenter);
                explosions.add(explosionCenter);
                explosionCenter.startExplosion();
            }

            if((tile = map.getTile(new Vector(position.getPosX(), position.getPosY() + (i+1)))).getState() != TileState.BLOCKED){
                if(tile.getContent() instanceof DestructibleWall){
                    ((DestructibleWall) tile.getContent()).destroy();
                }
                Explosion explosionUp = new Explosion(canvas, new Vector(position.getPosX(), position.getPosY() + (i+1)), map);
                map.setTileAsPassage(position.getPosX(), position.getPosY() + (i+1));
                map.getTile(explosionUp.getPosition()).setContent(explosionUp);
                explosions.add(explosionUp);
                explosionUp.startExplosion();
            }

            if((tile = map.getTile(new Vector(position.getPosX(), position.getPosY() - (i+1)))).getState() != TileState.BLOCKED){
                if(tile.getContent() instanceof DestructibleWall){
                    ((DestructibleWall) tile.getContent()).destroy();
                }
                Explosion explosionDown = new Explosion(canvas, new Vector(position.getPosX(), position.getPosY() - (i+1)), map);
                map.setTileAsPassage(position.getPosX(), position.getPosY() - (i+1));
                map.getTile(explosionDown.getPosition()).setContent(explosionDown);
                explosions.add(explosionDown);
                explosionDown.startExplosion();
            }

            if((tile = map.getTile(new Vector(position.getPosX() - (i+1), position.getPosY()))).getState() != TileState.BLOCKED){
                if(tile.getContent() instanceof DestructibleWall){
                    ((DestructibleWall) tile.getContent()).destroy();
                }
                Explosion explosionLeft = new Explosion(canvas, new Vector(position.getPosX() - (i+1), position.getPosY()), map);
                map.setTileAsPassage(position.getPosX() - (i+1), position.getPosY());
                map.getTile(explosionLeft.getPosition()).setContent(explosionLeft);
                explosions.add(explosionLeft);
                explosionLeft.startExplosion();
            }

            if((tile = map.getTile(new Vector(position.getPosX() + (i+1), position.getPosY()))).getState() != TileState.BLOCKED) {
                if (tile.getContent() instanceof DestructibleWall) {
                    ((DestructibleWall) tile.getContent()).destroy();
                }
                Explosion explosionRight = new Explosion(canvas, new Vector(position.getPosX() + (i + 1), position.getPosY()), map);
                map.setTileAsPassage(position.getPosX() + (i + 1), position.getPosY());
                map.getTile(explosionRight.getPosition()).setContent(explosionRight);
                explosions.add(explosionRight);
                explosionRight.startExplosion();
            }
        }

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

    public void updateFireRange() {
        if(fireRange < MAX_FIRE_RANGE) {
            this.fireRange++;
        }
    }

    public ArrayList<Explosion> getExplosions() {
        return explosions;
    }


    @Override
    public void onCollision(GameEntity other) {

    }

    @Override
    public void paint() {
        updateHitBox();

        gc.drawImage(idle.get(currentFrame), position.getPosX() * Tile.TILE_WIDTH, position.getPosY() * Tile.TILE_HEIGHT);
}

}