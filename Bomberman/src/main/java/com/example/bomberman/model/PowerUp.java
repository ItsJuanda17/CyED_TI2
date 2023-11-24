package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Objects;

public class PowerUp extends GameEntity{

    public enum PowerUpType {
        BOMB_COUNT(new Image(Objects.requireNonNull(PowerUpType.class.getResourceAsStream("/images/powerUps/power_bomb.png")))),
        BOMB_RANGE(new Image(Objects.requireNonNull(PowerUpType.class.getResourceAsStream("/images/powerUps/power_fireup.png")))),
        SPEED(new Image(Objects.requireNonNull(PowerUpType.class.getResourceAsStream("/images/powerUps/power_speed.png"))));

        private Image image;
        PowerUpType(Image image) {
        }
        public Image getImage() {
            return image;
        }
    }

    private PowerUpType type;

    public PowerUp(Canvas canvas, Vector position, PowerUpType type) {
        super(canvas);
        this.position = position;
        this.type = type;
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
            case SPEED:
                player.incrementSpeed();
                break;
        }
    }

    @Override
    public void onCollision(GameEntity other) {
        if(other instanceof Player) {
            grantBonus((Player) other);
        }
    }

    @Override
    public void paint() {
        gc.drawImage(this.type.getImage(), this.position.getPosX(), this.position.getPosY(), this.width, this.height);
    }

}