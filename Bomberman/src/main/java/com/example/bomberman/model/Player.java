package com.example.bomberman.model;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.Objects;

public class Player extends Character {

    private ArrayList<Bomb> bombs;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private CharacterState lastDirection;

    public Player(Canvas canvas, Map map) {
        super(canvas, map);
        initPlayer();
    }

    private void initPlayer(){
        this.bombs = new ArrayList<>();
        this.width = WIDTH;
        this.height = HEIGHT;
        this.speed = 1;
        this.lives = 3;
        this.state = CharacterState.IDLE;
        this.lastDirection = CharacterState.RUN_DOWN;
        this.frame = 0;
        this.position = new Vector(map.getSpawnPoint().getX(), map.getSpawnPoint().getY());

        for(int i = 0; i < 4; i++) {
            this.idle.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bomberman/idle/bomberman-idle-0"+i+".png")), WIDTH, HEIGHT, false, false));
        }

        for(int i = 0; i < 4; i++) {
            this.runUp.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bomberman/run/bomberman-run-up-0" + i + ".png")), WIDTH, HEIGHT, false, false));
        }

        for(int i = 0; i < 4; i++) {
            this.runDown.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bomberman/run/bomberman-run-down-0" + i + ".png")), WIDTH, HEIGHT, false, false));
        }

        for(int i = 0; i < 4; i++) {
            this.runLeft.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bomberman/run/bomberman-run-left-0" + i + ".png")), WIDTH, HEIGHT, false, false));
        }

        for(int i = 0; i < 4; i++) {
            this.runRight.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bomberman/run/bomberman-run-right-0" + i + ".png")), WIDTH, HEIGHT, false, false));
        }
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    @Override
    public void move() {
        int moveAmount = 1;

        if (upPressed) {
            if(!map.isNextTileBlocked(position.getPosX(), position.getPosY() - moveAmount)) {
                position.setPosY(position.getPosY() - moveAmount);
            }
        }
        if (downPressed) {
            if(!map.isNextTileBlocked(position.getPosX(), position.getPosY() + moveAmount)) {
                position.setPosY(position.getPosY() + moveAmount);
            }
        }
        if (leftPressed) {
            if(!map.isNextTileBlocked(position.getPosX()  - moveAmount, position.getPosY())) {
                position.setPosX(position.getPosX() - moveAmount);
            }
        }
        if (rightPressed) {
            if(!map.isNextTileBlocked(position.getPosX() + moveAmount, position.getPosY())) {
                position.setPosX(position.getPosX() + moveAmount);
            }
        }
    }

    @Override
    public void die() {
        isDead = true;
        //logic for images
    }

    @Override
    public void paint() {
        move();
        updateHitBox();
        switch (state) {
            case IDLE -> {
                switch (lastDirection) {
                    case RUN_UP -> gc.drawImage(idle.get(0), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                    case RUN_DOWN -> gc.drawImage(idle.get(1), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                    case RUN_LEFT -> gc.drawImage(idle.get(2), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                    case RUN_RIGHT -> gc.drawImage(idle.get(3), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                }
            }
            case RUN_UP -> {
                gc.drawImage(runUp.get(frame), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                frame = (frame + 1) % runUp.size();
            }
            case RUN_DOWN -> {
                gc.drawImage(runDown.get(frame), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                frame = (frame + 1) % runDown.size();
            }
            case RUN_LEFT -> {
                gc.drawImage(runLeft.get(frame), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                frame = (frame + 1) % runLeft.size();
            }
            case RUN_RIGHT -> {
                gc.drawImage(runRight.get(frame), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                frame = (frame + 1) % runRight.size();
            }
        }
    }

    public void incrementBombCount() {
        Bomb newBomb = new Bomb(this.canvas, this.map, this.getPosition().getPosX(), this.getPosition().getPosY());
        newBomb.dropBomb(3, 1, this.getPosition().getPosX(), this.getPosition().getPosY());
        this.bombs.add(newBomb);
    }


    public void incrementBombRange() {
        int newRange = 2;
        this.bombs.forEach(bomb -> bomb.updateFireRange(newRange));
    }

    public void incrementSpeed() {
        this.speed++;
    }

    public void setOnKeyPressed(KeyEvent event) {
        KeyCode keyPressed = event.getCode();
        switch (keyPressed) {
            case UP -> {
                state = lastDirection = CharacterState.RUN_UP;
                upPressed = true;
            }
            case DOWN -> {
                state = lastDirection = CharacterState.RUN_DOWN;
                downPressed = true;
            }
            case LEFT -> {
                state = lastDirection = CharacterState.RUN_LEFT;
                leftPressed = true;
            }
            case RIGHT -> {
                state = lastDirection = CharacterState.RUN_RIGHT;
                rightPressed = true;
            }
            case SPACE -> {
                incrementBombCount();
            }
        }
    }

    public void setOnKeyReleased(KeyEvent event) {
        state = CharacterState.IDLE;
        switch (event.getCode()) {
            case UP -> {
                upPressed = false;
            }
            case DOWN -> {
                downPressed = false;
            }
            case LEFT -> {
                leftPressed = false;
            }
            case RIGHT -> {
                rightPressed = false;
            }
        }
    }

    @Override
    public void onCollision(GameEntity other) {
        if (other instanceof Enemy || other instanceof Explosion) {
            this.lives--;

            if (this.lives > 0) {
                this.position = new Vector(map.getSpawnPoint().getX(), map.getSpawnPoint().getY());
            } else if (this.lives == 0) {
                die();
            }
        } else if (other instanceof PowerUp) {
            other.onCollision(this);
        }
    }

}