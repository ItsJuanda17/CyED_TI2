package com.example.bomberman.model;

import com.example.bomberman.collections.IGraph;
import com.example.bomberman.collections.ListGraph;

import com.example.bomberman.control.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Map {

    public final static int WIDTH = (int) (GameController.GAME_WIDTH / Tile.TILE_WIDTH);
    public final static int HEIGHT = (int) (GameController.GAME_HEIGHT / Tile.TILE_HEIGHT);
    private Canvas canvas;
    private GraphicsContext gc;
    private Tile spawnPoint;
    private Tile exitPoint;
    private Image exitImage;
    private RandomMapGenerator randomMapGenerator;
    private IGraph<Vector, Tile> map;
    private Player player;
    private Bomb bomb;
    private ArrayList<Enemy> enemies;

    public Map(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.map = new ListGraph<>();
        this.randomMapGenerator = new RandomMapGenerator(canvas, WIDTH, HEIGHT);
        createMap();

        this.enemies = new ArrayList<>();
        Random random = new Random();

        int enemyCount = random.nextInt(3) + 5;
        for(int i = 0; i < enemyCount; i++){
            Enemy enemy = new Enemy(canvas, this, getRandomPosition());
            enemies.add(enemy);
        }

        this.player = new Player(canvas, this);
        this.bomb = new Bomb(canvas);
        this.exitImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/walls/exit-00.png")), Tile.TILE_WIDTH, Tile.TILE_HEIGHT, false, false);
    }

    public Vector getRandomPosition() {
        Random random = new Random(); int x; int y; Vector position;
        do {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT);
            position = new Vector(x, y);
        } while (map.getVertex(position).getValue().getState() != TileState.PASSAGE || position.equals(spawnPoint.getPosition()));

        return position;
    }

    public IGraph<Vector, Tile> getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public GameEntity getExitPoint() {
        return exitPoint;
    }

    private void createMap() {
        Tile[][] grid = randomMapGenerator.getGrid();

        this.spawnPoint = randomMapGenerator.getStartTile();
        this.exitPoint = randomMapGenerator.getExitTile();

        System.out.println("Spawn point: " + spawnPoint.getPosition());
        System.out.println(WIDTH+ " " + HEIGHT);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Tile currentTile = grid[x][y];
                Vector position = new Vector(x, y);
                map.addVertex(position, currentTile);
            }
        }

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Vector position = new Vector(x, y);

                // Check neighboring tiles and add edges if they are passages
                if (x > 0 && grid[x - 1][y].getState() == TileState.PASSAGE) {
                    map.addEdge(position, new Vector(x - 1, y), 1);
                }
                if (x < WIDTH - 1 && grid[x + 1][y].getState() == TileState.PASSAGE) {
                    map.addEdge(position, new Vector(x + 1, y), 1);
                }
                if (y > 0 && grid[x][y - 1].getState() == TileState.PASSAGE) {
                    map.addEdge(position, new Vector(x, y - 1), 1);
                }
                if (y < HEIGHT - 1 && grid[x][y + 1].getState() == TileState.PASSAGE) {
                    map.addEdge(position, new Vector(x, y + 1), 1);
                }
            }
        }
    }

    public boolean collidesWithWalls(Player player) {

        int playerX = player.getPosition().getPosX();
        int playerY = player.getPosition().getPosY();
        int playerSize = 10;


        for (Tile tile : map.getVertices()) {
            if (tile.getState() == TileState.BLOCKED) {
                int wallX = tile.getX() * 10;
                int wallY = tile.getY() * 10;

                if (playerX < wallX + 10 &&
                        playerX + playerSize > wallX &&
                        playerY < wallY + 10 &&
                        playerY + playerSize > wallY) {
                    return true;
                }
            }
        }

        return false;
    }

    public Tile getSpawnPoint() {
        return spawnPoint;
    }

    public void paint(){
        paintMap();
        player.paint();
        bomb.paint();
        for(Enemy enemy : enemies){
            enemy.paint();
            Thread thread = new Thread(enemy);
            thread.start();
        }
    }

    private void paintMap(){
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for(Tile tile : map.getVertices()){
            if(!tile.equals(spawnPoint) && !tile.equals(exitPoint))
                tile.paint();
        }
        spawnPoint.getGc().setFill(Color.BLUE);
        spawnPoint.getGc().fillRect(spawnPoint.getX() * Tile.TILE_WIDTH, spawnPoint.getY() * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
        exitPoint.getGc().setFill(Color.RED);
        exitPoint.getGc().drawImage(exitImage, exitPoint.getX() * Tile.TILE_WIDTH, exitPoint.getY() * Tile.TILE_HEIGHT);
    }

    public void setOnKeyPressed(KeyEvent event){
        player.setOnKeyPressed(event);
    }

    public void setOnKeyReleased(KeyEvent event){
        player.setOnKeyReleased(event);
    }


}
