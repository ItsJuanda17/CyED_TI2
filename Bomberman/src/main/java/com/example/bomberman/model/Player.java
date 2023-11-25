package com.example.bomberman.model;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class Player extends Character {

    private static final int MAX_BOMBS = 5;
    private Stack<Bomb> bombs;
    private int numBombs;
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
        this.bombs = new Stack<>();
        this.bombs.add(new Bomb(canvas, map));
        this.numBombs = 1;
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

    public Stack<Bomb> getBombs() {
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
        if(numBombs<MAX_BOMBS) {
            Bomb newBomb = new Bomb(this.canvas, this.map);
            this.bombs.add(newBomb);
            this.numBombs++;
        }
    }

    public void incrementBombRange() {
        this.bombs.forEach(Bomb::updateFireRange);
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
                dropBomb();
            }
        }
    }

    private void dropBomb() {
        Tile toPlaceTile = map.getTile(this.position);
        if(!(toPlaceTile.getContent() instanceof Bomb)) {
            map.getTile(position).setContent(bombs.peek());
            bombs.pop().dropBomb(new Vector(position.getPosX(), position.getPosY()));
            if(bombs.isEmpty()) {
                bombs.add(new Bomb(canvas, map));
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
