package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Objects;

public class DestructibleWall extends Wall {

    private TileState state;

    public DestructibleWall(Canvas canvas, int x, int y) {
        super(canvas, x, y);
        this.state = TileState.BREAKABLE_WALL;
    }

    @Override
    public void placeImage() {
        idle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/walls/destructibleWall-idle-00.png")), Tile.TILE_WIDTH, Tile.TILE_HEIGHT, false, false);
    }

}
