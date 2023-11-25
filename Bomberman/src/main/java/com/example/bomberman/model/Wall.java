package com.example.bomberman.model;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Objects;

public class Wall extends GameEntity{

    protected boolean destroyed;
    protected Image idle;

    public Wall(Canvas canvas, int x, int y) {
        super(canvas);
        this.destroyed = false;
        this.position = new Vector(x, y);
        placeImage();
    }

    public void placeImage() {
        idle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/walls/wall-idle-00.png")), Tile.TILE_WIDTH, Tile.TILE_HEIGHT, false, false);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void onCollision(GameEntity other) {
        if(other instanceof Player) {
            onCollision(other);
        }
    }

    @Override
    public void paint() {
        updateHitBox();
        paintHitBox();
        gc.drawImage(idle, position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
    }
}
