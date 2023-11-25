package com.example.bomberman.model;

import com.example.bomberman.collections.IGraph;
import com.example.bomberman.collections.ListGraph;

import com.example.bomberman.control.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.*;

public class Map {

    public final static int WIDTH = (int) (GameController.GAME_WIDTH / Tile.TILE_WIDTH);
    public final static int HEIGHT = (int) (GameController.GAME_HEIGHT / Tile.TILE_HEIGHT);
    private static final int SPAWN_AREA_SIZE = 2;
    private Canvas canvas;
    private GraphicsContext gc;
    private Tile spawnPoint;
    private Tile exitPoint;
    private Image exitImage;
    private RandomMapGenerator randomMapGenerator;
    private IGraph<Vector, Tile> map;
    private Tile[][] grid;
    private Player player;
    private ArrayList<Enemy> enemies;

    public Map(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.map = new ListGraph<>();
        this.randomMapGenerator = new RandomMapGenerator(canvas, WIDTH, HEIGHT);
        createMap();
        initializeSpawnArea();
        this.player = new Player(canvas, this);
        generateEnemies();
        generateRandomPowerUps(5);
        this.exitPoint = map.getVertex(getRandomPosition()).getValue();
        this.exitImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/walls/exit-00.png")), Tile.TILE_WIDTH, Tile.TILE_HEIGHT, false, false);
    }

    private void initializeSpawnArea() {
        this.spawnPoint = map.getVertex(getRandomPosition()).getValue();
        int x = spawnPoint.getX();
        int y = spawnPoint.getY();

        // Set the spawn area as passages and surround it with breakable walls
        for (int i = x - SPAWN_AREA_SIZE; i <= x + SPAWN_AREA_SIZE; i++) {
            for (int j = y - SPAWN_AREA_SIZE; j <= y + SPAWN_AREA_SIZE; j++) {
                if (isWithinBounds(i, j) && !isOnMapEdge(i, j)) {
                    if (i == x - SPAWN_AREA_SIZE || i == x + SPAWN_AREA_SIZE || j == y - SPAWN_AREA_SIZE || j == y + SPAWN_AREA_SIZE) {
                        setTileAsBreakableWall(i, j);
                    } else {
                        setTileAsPassage(i, j);
                    }
                }
            }
        }
    }

    private boolean isOnMapEdge(int x, int y) {
        return x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }


    private void setTileAsBreakableWall(int posX, int posY) {
        Vector position = new Vector(posX, posY);
        Tile tile = map.getVertex(position).getValue();
        tile.setState(TileState.BREAKABLE_WALL);
        int x = position.getPosX();
        int y = position.getPosY();
        if (x > 0 && grid[x - 1][y].getState() == TileState.PASSAGE) {
            map.removeEdge(position, new Vector(x - 1, y));
            map.removeEdge(new Vector(x - 1, y), position);
        }
        if (x < WIDTH - 1 && grid[x + 1][y].getState() == TileState.PASSAGE) {
            map.removeEdge(position, new Vector(x + 1, y));
            map.removeEdge(new Vector(x + 1, y), position);
        }
        if (y > 0 && grid[x][y - 1].getState() == TileState.PASSAGE) {
            map.removeEdge(position, new Vector(x, y - 1));
            map.removeEdge(new Vector(x, y - 1), position);
        }
        if (y < HEIGHT - 1 && grid[x][y + 1].getState() == TileState.PASSAGE) {
            map.removeEdge(position, new Vector(x, y + 1));
            map.removeEdge(new Vector(x, y + 1), position);
        }
    }

    public void setTileAsPassage(int posX, int posY) {
        Vector position = new Vector(posX, posY);
        Tile tile = map.getVertex(position).getValue();
        tile.setState(TileState.PASSAGE);
        tile.setContent(null);
        int x = position.getPosX();
        int y = position.getPosY();
        if (x > 0 && grid[x - 1][y].getState() == TileState.PASSAGE) {
            map.addEdge(position, new Vector(x - 1, y), 1);
            map.addEdge(new Vector(x - 1, y), position,1 );
        }
        if (x < WIDTH - 1 && grid[x + 1][y].getState() == TileState.PASSAGE) {
            map.addEdge(position, new Vector(x + 1, y), 1);
            map.addEdge(new Vector(x + 1, y), position, 1);
        }
        if (y > 0 && grid[x][y - 1].getState() == TileState.PASSAGE) {
            map.addEdge(position, new Vector(x, y - 1), 1);
            map.addEdge(new Vector(x, y - 1), position, 1);
        }
        if (y < HEIGHT - 1 && grid[x][y + 1].getState() == TileState.PASSAGE) {
            map.addEdge(position, new Vector(x, y + 1), 1);
            map.addEdge(new Vector(x, y + 1), position, 1);
        }
    }

    private void generateEnemies() {
        this.enemies = new ArrayList<>();
        Random random = new Random();

        int enemyCount = random.nextInt(3) + 3;

        for (int i = 0; i < enemyCount; i++) {
            Vector enemyPosition;
            do {
                enemyPosition = getRandomPosition();
            } while (isInsideSpawnArea(enemyPosition));

            Enemy enemy = new Enemy(canvas, this, enemyPosition);
            enemies.add(enemy);
        }
    }

    private boolean isInsideSpawnArea(Vector position) {
        int x = position.getPosX();
        int y = position.getPosY();
        int spawnX = spawnPoint.getX();
        int spawnY = spawnPoint.getY();

        return x >= spawnX - SPAWN_AREA_SIZE && x <= spawnX + SPAWN_AREA_SIZE
                && y >= spawnY - SPAWN_AREA_SIZE && y <= spawnY + SPAWN_AREA_SIZE;
    }

    public Vector getRandomPosition() {
        Random random = new Random(); int x; int y; Vector position;
        do {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT);
            position = new Vector(x, y);
        } while (map.getVertex(position).getValue().getState() != TileState.PASSAGE);

        return position;
    }

    public void generateRandomPowerUps(int numPowerUps) {
        Random random = new Random();
        for (int i = 0; i < numPowerUps; i++) {
            PowerUp.PowerUpType randomType = PowerUp.PowerUpType.values()[random.nextInt(PowerUp.PowerUpType.values().length)];
            PowerUp powerUp = new PowerUp(canvas, getRandomPosition(), randomType, this);
            getTile(powerUp.getPosition()).setContent(powerUp);
        }
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
        this.grid = randomMapGenerator.getGrid();

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

    public Tile getSpawnPoint() {
        return spawnPoint;
    }

    public void paint() {
        paintMap();
        player.paint();

        for (Enemy enemy : enemies) {
            enemy.paint();
        }

        checkCollisions();
    }

    private void paintMap(){
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for(Tile tile : map.getVertices()){
            if(!tile.equals(spawnPoint) && !tile.equals(exitPoint))
                tile.paint();
        }
        spawnPoint.getGc().fillRect(spawnPoint.getX() * Tile.TILE_WIDTH, spawnPoint.getY() * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
        exitPoint.getGc().drawImage(exitImage, exitPoint.getX() * Tile.TILE_WIDTH, exitPoint.getY() * Tile.TILE_HEIGHT);
    }

    public void setOnKeyPressed(KeyEvent event){
        player.setOnKeyPressed(event);
    }

    public void setOnKeyReleased (KeyEvent event){
        player.setOnKeyReleased(event);
    }

    public void checkCollisions() {
        Tile playerActualTile = map.getVertex(player.getPosition()).getValue();
        if (player.intersect(playerActualTile)) {
            playerActualTile.onCollision(player);
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            Tile enemyActualTile = map.getVertex(enemy.getPosition()).getValue();

            if (enemy.intersect(player)) {
                enemy.onCollision(player);
            }

            if (enemy.intersect(enemyActualTile)) {
                enemyActualTile.onCollision(enemy);
            }

            if (enemy.isDead()) {
                enemyIterator.remove();
            }
        }

    }


    public boolean isNextTileBlocked(int x, int y) {
        Tile tile = map.getVertex(new Vector(x, y)).getValue();
        return tile.getState() != TileState.PASSAGE || tile.getContent() instanceof Bomb;
    }

    public Tile getTile(Vector position) {
        return map.getVertex(position).getValue();
    }
}
