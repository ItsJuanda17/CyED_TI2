package com.example.bomberman.model;

import com.example.bomberman.collections.Vertex;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Comparator;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

public class Enemy extends Character implements Runnable{

    private final static int RANGE_OF_SIGHT = 3;
    Queue<Vertex<Vector, Tile>> path;

    public Enemy(Canvas canvas, Map map, Vector startPosition) {
        super(canvas, map);
        this.position = startPosition;
        initEnemy();
    }

    private void initEnemy() {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.speed = 1;
        this.lives = 1;
        this.state = CharacterState.IDLE;
        this.frame = 0;
        this.path = map.getMap().bfs(position);

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
    public void move(){
        Vertex<Vector, Tile> nextVertex = path.poll();

        if(nextVertex == null) return;
        Vector nextPosition = nextVertex.getKey();
        moveTowards(nextPosition);

        Queue<Vertex<Vector, Tile>> newPath = map.getMap().bfs(map.getPlayer().getPosition());
        for(Vertex<Vector, Tile> vertex : newPath){
            if(vertex.getDistance() < RANGE_OF_SIGHT){
                path = newPath;
                nextPosition = path.poll().getKey();
                moveTowardsPlayer(nextPosition);
                break;
            }
        }
    }

    private void moveTowards(Vector targetPosition) {
        while (!position.equals(targetPosition)) {
            int newPosX = position.getPosX();
            int newPosY = position.getPosY();

            Random random = new Random();
            int randomDirection = random.nextInt(4);

            switch (randomDirection) {
                case 0:
                    if (targetPosition.getPosX() < position.getPosX()) {
                        state = CharacterState.RUN_LEFT;
                        Vector nextPos = new Vector(position.getPosX() - 1, position.getPosY());
                        if (isValidMove(nextPos)) {
                            newPosX--;
                        }
                    }
                    break;
                case 1:
                    if (targetPosition.getPosX() > position.getPosX()) {
                        state = CharacterState.RUN_RIGHT;
                        Vector nextPos = new Vector(position.getPosX() + 1, position.getPosY());
                        if (isValidMove(nextPos)) {
                            newPosX++;
                        }
                    }
                    break;
                case 2:
                    if (targetPosition.getPosY() < position.getPosY()) {
                        state = CharacterState.RUN_UP;
                        Vector nextPos = new Vector(position.getPosX(), position.getPosY() - 1);
                        if (isValidMove(nextPos)) {
                            newPosY--;
                        }
                    }
                    break;
                case 3:
                    if (targetPosition.getPosY() > position.getPosY()) {
                        state = CharacterState.RUN_DOWN;
                        Vector nextPos = new Vector(position.getPosX(), position.getPosY() + 1);
                        if (isValidMove(nextPos)) {
                            newPosY++;
                        }
                    }
                    break;
            }

            position.setPosX(newPosX);
            position.setPosY(newPosY);

            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void moveTowardsPlayer(Vector targetPosition){
        while(!position.equals(targetPosition)){
            int newPosX = position.getPosX();
            int newPosY = position.getPosY();

            if(targetPosition.getPosX() < position.getPosX()){
                state = CharacterState.RUN_LEFT;
                Vector nextPos = new Vector(position.getPosX() - 1, position.getPosY());
                if(isValidMove(nextPos)){
                    newPosX--;
                }
            }

            if(targetPosition.getPosX() > position.getPosX()){
                state = CharacterState.RUN_RIGHT;
                Vector nextPos = new Vector(position.getPosX() + 1, position.getPosY());
                if(isValidMove(nextPos)){
                    newPosX++;
                }
            }
            if(targetPosition.getPosY() < position.getPosY()){
                state = CharacterState.RUN_UP;
                Vector nextPos = new Vector(position.getPosX(), position.getPosY() - 1);
                if(isValidMove(nextPos)){
                    newPosY--;
                }
            }
            if(targetPosition.getPosY() > position.getPosY()){
                state = CharacterState.RUN_DOWN;
                Vector nextPos = new Vector(position.getPosX(), position.getPosY() + 1);
                if(isValidMove(nextPos)){
                    newPosY++;
                }
            }

            position.setPosX(newPosX);
            position.setPosY(newPosY);

            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean isValidMove(Vector position) {
        return position.getPosX() >= 0 && position.getPosX() < Map.WIDTH &&
                position.getPosY() >= 0 && position.getPosY() < Map.HEIGHT &&
                !map.isNextTileBlocked(position.getPosX(), position.getPosY());
    }

    @Override
    public void die() {

    }

    @Override
    public void onCollision(GameEntity other) {
        if(other instanceof Player){
            other.onCollision(this);
        }else if (other instanceof Explosion){
            other.onCollision(this);
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
            if(path.isEmpty()) path = map.getMap().bfs(position);
            move();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
  }
}

}