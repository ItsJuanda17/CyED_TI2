package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Objects;

public class PowerUp extends GameEntity {

    public enum PowerUpType {
        BOMB_COUNT(new Image(Objects.requireNonNull(PowerUpType.class.getResourceAsStream("/images/powerUps/power_bomb.png")))),
        BOMB_RANGE(new Image(Objects.requireNonNull(PowerUpType.class.getResourceAsStream("/images/powerUps/power_fireup.png"))));

        private final Image image;

        PowerUpType(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }
    }

    private PowerUpType type;
    private Image image;
    private Map map;

    public PowerUp(Canvas canvas, Vector position, PowerUpType type, Map map) {
        super(canvas);
        this.position = position;
        this.type = type;
        this.map = map;
        this.image = type.getImage();
    }

    public PowerUpType getType() {
        return type;
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }

    public void grantBonus(Player player) {
        switch (this.type) {
            case BOMB_COUNT:
                player.incrementBombCount();
                break;
            case BOMB_RANGE:
                player.incrementBombRange();
                break;
        }
    }

    @Override
    public void onCollision(GameEntity other) {
        if (other instanceof Player) {
            grantBonus((Player) other);
            map.getTile(position).setContent(null);
        }
    }

    @Override
    public void paint() {
        gc.drawImage(image, this.position.getPosX()*Tile.TILE_WIDTH, this.position.getPosY()*Tile.TILE_HEIGHT, Tile.TILE_WIDTH-5, Tile.TILE_HEIGHT-5);
    }

}
