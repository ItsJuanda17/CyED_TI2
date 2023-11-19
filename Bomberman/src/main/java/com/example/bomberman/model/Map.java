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
import java.util.Objects;

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
        this.enemies = new ArrayList<>();
        enemies.add(new Enemy(canvas, this));
        createMap();
        this.player = new Player(canvas, this);
        this.bomb = new Bomb(canvas);
        this.exitImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/walls/exit-00.png")), Tile.TILE_WIDTH, Tile.TILE_HEIGHT, false, false);

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

    private void createMap() {
        Tile[][] grid = randomMapGenerator.getGrid();

        this.spawnPoint = randomMapGenerator.getStartTile();
        this.exitPoint = randomMapGenerator.getExitTile();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Tile currentTile = grid[x][y];

                // Add the current tile as a vertex in the graph
                map.addVertex(new Vector(x, y), currentTile);

                // Check neighboring tiles and add edges if they are passages
                if (x > 0 && grid[x - 1][y].getState() == TileState.PASSAGE) {
                    map.addEdge(new Vector(x, y), new Vector(x - 1, y), 1);
                }
                if (x < WIDTH - 1 && grid[x + 1][y].getState() == TileState.PASSAGE) {
                    map.addEdge(new Vector(x, y), new Vector(x + 1, y), 1);
                }
                if (y > 0 && grid[x][y - 1].getState() == TileState.PASSAGE) {
                    map.addEdge(new Vector(x, y), new Vector(x, y - 1), 1);
                }
                if (y < HEIGHT - 1 && grid[x][y + 1].getState() == TileState.PASSAGE) {
                    map.addEdge(new Vector(x, y), new Vector(x, y + 1), 1);
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
