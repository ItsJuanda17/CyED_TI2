package com.example.bomberman.model;

import com.example.bomberman.collections.IGraph;
import com.example.bomberman.collections.ListGraph;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map {

    private Canvas canvas;
    private GraphicsContext gc;
    private IGraph<Vector, Tile> map;
    private Tile spawnPoint;
    private Tile exitPoint;
    private int width;
    private int height;

    public Map(Canvas canvas, int width, int height) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        map = new ListGraph<>();
        this.width = width;
        this.height = height;
        createMap();
    }

    public IGraph<Vector, Tile> getMap() {
        return map;
    }

    private void initializeMap() {
        for (int i = 0; i < width; i++) {
            map.addVertex(new Vector(i, 0), new Tile(i, 0));
            map.addVertex(new Vector(i, height - 1), new Tile(i, height - 1));
        }
        for (int i = 1; i < height - 1; i++) {
            map.addVertex(new Vector(0, i), new Tile(0, i));
            map.addVertex(new Vector(width - 1, i), new Tile(width - 1, i));
        }
        for (int i = 2; i < height - 2; i += 2) {
            for (int j = 2; j < width - 2; j += 2) {
                map.addVertex(new Vector(j, i), new Tile(j, i));
            }
        }
    }

    private void createMap() {
        initializeMap();
        RandomMapGenerator randomMapGenerator = new RandomMapGenerator(width, height);
        Tile[][] grid = randomMapGenerator.generateMap();
        spawnPoint = randomMapGenerator.getStartTile();
        exitPoint = randomMapGenerator.getExitTile();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile currentTile = grid[x][y];
                if (currentTile.getState() == TileState.PASSAGE) {
                    // Connect this tile to its neighbors in the graph.
                    if (x > 0 && grid[x - 1][y].getState() == TileState.PASSAGE) {
                        map.addEdge(currentTile.getPosition(), grid[x - 1][y].getPosition(), 1);
                    }
                    if (x < width - 1 && grid[x + 1][y].getState() == TileState.PASSAGE) {
                        map.addEdge(currentTile.getPosition(), grid[x + 1][y].getPosition(),1 );
                    }
                    if (y > 0 && grid[x][y - 1].getState() == TileState.PASSAGE) {
                        map.addEdge(currentTile.getPosition(), grid[x][y - 1].getPosition(),1 );
                    }
                    if (y < height - 1 && grid[x][y + 1].getState() == TileState.PASSAGE) {
                        map.addEdge(currentTile.getPosition(), grid[x][y + 1].getPosition(),1 );
                    }
                }
            }
        }
    }

    public void paint(){
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for(Tile tile : map.getVertices()){
            tile.paint(this.canvas);
        }
    }
}
