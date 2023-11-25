package com.example.bomberman.model;

import com.example.bomberman.collections.Vertex;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.*;

public class Enemy extends Character implements Runnable{

    private final static int RANGE_OF_SIGHT = 3;
    private boolean isChasingPlayer;
    private Queue<Vertex<Vector, Tile>> path;
    private Vector targetPosition;
    private Thread thread;

    public Enemy(Canvas canvas, Map map, Vector startPosition) {
        super(canvas, map);
        this.position = startPosition;
        initEnemy();
    }

    public void start(){
        thread = new Thread(this);
        thread.start();
    }

    private void initEnemy() {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.speed = 1;
        this.lives = 1;
        this.state = CharacterState.IDLE;
        this.frame = 0;
        this.path = new LinkedList<>();
        this.isChasingPlayer = false;
        this.targetPosition = position;
        start();

        this.idle.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/idle/enemy-idle-00.png")), WIDTH, HEIGHT, false, false));

        for(int i = 0; i < 2; i++) {
            this.runUp.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/run/enemy-run-up-0" + i + ".png")), WIDTH, HEIGHT, false, false));
        }

        for(int i = 0; i < 2; i++) {
            this.runDown.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/run/enemy-run-down-0" + i + ".png")), WIDTH, HEIGHT, false, false));
        }

        for (int i = 0; i < 2; i++) {
            this.runLeft.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/run/enemy-run-left-0" + i + ".png")), WIDTH, HEIGHT, false, false));
        }

        for (int i = 0; i < 2; i++) {
            this.runRight.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy/run/enemy-run-right-0" + i + ".png")), WIDTH, HEIGHT, false, false));
        }
    }

    @Override
    public void move() {
        if (position.equals(targetPosition)) {
            Vertex<Vector, Tile> nextVertex = path.poll();
            if (nextVertex == null) return;
            this.targetPosition = nextVertex.getKey();
        }

        if (isChasingPlayer) {
            moveTowardsPlayer(targetPosition);
            double distanceToPlayer = position.distanceTo(map.getPlayer().getPosition());
            if (distanceToPlayer > RANGE_OF_SIGHT) {
                isChasingPlayer = false;
                path = map.getMap().bfs(position);
                if (!path.isEmpty()) {
                    this.targetPosition = path.poll().getKey();
                }
            }
        } else {
            moveTowards(targetPosition);
            double distanceToPlayer = position.distanceTo(map.getPlayer().getPosition());
            if (distanceToPlayer <= RANGE_OF_SIGHT) {
                isChasingPlayer = true;
                path = map.getMap().bfs(map.getPlayer().getPosition());
                if (!path.isEmpty()) {
                    this.targetPosition = path.poll().getKey();
                }
            }
        }
    }

    private void moveTowards(Vector targetPosition) {
        int newPosX = position.getPosX();
        int newPosY = position.getPosY();

        Random random = new Random();
        int randomDirection = random.nextInt(4);

        switch (randomDirection) {
            case 0:
                if (targetPosition.getPosX() < position.getPosX()) {
                    Vector nextPos = new Vector(position.getPosX() - 1, position.getPosY());
                    if (isValidMove(nextPos)) {
                        state = CharacterState.RUN_LEFT;
                        newPosX--;
                    }
                }
                break;
            case 1:
                if (targetPosition.getPosX() > position.getPosX()) {
                    Vector nextPos = new Vector(position.getPosX() + 1, position.getPosY());
                    if (isValidMove(nextPos)) {
                        state = CharacterState.RUN_RIGHT;
                        newPosX++;
                    }
                }
                break;
            case 2:
                if (targetPosition.getPosY() < position.getPosY()) {
                    Vector nextPos = new Vector(position.getPosX(), position.getPosY() - 1);
                    if (isValidMove(nextPos)) {
                        state = CharacterState.RUN_UP;
                        newPosY--;
                    }
                }
                break;
            case 3:
                if (targetPosition.getPosY() > position.getPosY()) {
                    Vector nextPos = new Vector(position.getPosX(), position.getPosY() + 1);
                    if (isValidMove(nextPos)) {
                        state = CharacterState.RUN_DOWN;
                        newPosY++;
                    }
                }
                break;
        }

        position.setPosX(newPosX);
        position.setPosY(newPosY);
    }

    private void moveTowardsPlayer(Vector targetPosition){
        int newPosX = position.getPosX();
        int newPosY = position.getPosY();

        if(targetPosition.getPosX() < position.getPosX() && isValidMove(new Vector(position.getPosX() - 1, position.getPosY()))){
            state = CharacterState.RUN_LEFT;
            newPosX--;
        }else if(targetPosition.getPosX() > position.getPosX() && isValidMove(new Vector(position.getPosX() + 1, position.getPosY()))){
            state = CharacterState.RUN_RIGHT;
            newPosX++;
        }else if(targetPosition.getPosY() < position.getPosY() && isValidMove(new Vector(position.getPosX(), position.getPosY() - 1))){
            state = CharacterState.RUN_UP;
            newPosY--;
        }else if(targetPosition.getPosY() > position.getPosY() && isValidMove(new Vector(position.getPosX(), position.getPosY() + 1))){
            state = CharacterState.RUN_DOWN;
            newPosY++;
        }

        position.setPosX(newPosX);
        position.setPosY(newPosY);

    }

    private boolean isValidMove(Vector position) {
        return position.getPosX() >= 0 && position.getPosX() < Map.WIDTH &&
                position.getPosY() >= 0 && position.getPosY() < Map.HEIGHT &&
                !map.isNextTileBlocked(position.getPosX(), position.getPosY());
    }

    @Override
    public void die() {
        isDead = true;
        thread.interrupt();
    }

    @Override
    public void onCollision(GameEntity other) {
        if(other instanceof Player){
            other.onCollision(this);
        }else if (other instanceof Bomb){
            other.onCollision(this);
        }else if (other instanceof Explosion){
            lives--;
            if(lives == 0){
                die();
            }
        }
    }

    @Override
    public void paint() {
        updateHitBox();

        switch (state) {
            case IDLE:
                gc.drawImage(idle.get(0), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                break;
            case RUN_UP:
                gc.drawImage(runUp.get(frame), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                frame = (frame + 1) % runUp.size();
                break;
            case RUN_DOWN:
                gc.drawImage(runDown.get(frame), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                frame = (frame + 1) % runDown.size();
                break;
            case RUN_LEFT:
                gc.drawImage(runLeft.get(frame), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                frame = (frame + 1) % runLeft.size();
                break;
            case RUN_RIGHT:
                gc.drawImage(runRight.get(frame), position.getPosX()*Tile.TILE_WIDTH, position.getPosY()*Tile.TILE_HEIGHT);
                frame = (frame + 1) % runRight.size();
                break;
        }
    }

    @Override
    public void run() {
        while(!isDead){
            if(isChasingPlayer) {
                path = map.getMap().bfs(map.getPlayer().getPosition());
            }else if(path.isEmpty()){
                path = map.getMap().bfs(position);
            }
            move();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
