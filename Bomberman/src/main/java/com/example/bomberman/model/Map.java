package com.example.bomberman.model;

import com.example.bomberman.collections.IGraph;
import com.example.bomberman.collections.ListGraph;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map {

    private Canvas canvas;
    private GraphicsContext gc;
    private int width;
    private int height;
    private Tile spawnPoint;
    private Tile exitPoint;
    private RandomMapGenerator randomMapGenerator;
    private IGraph<Vector, Tile> map;


    public Map(Canvas canvas, int width, int height) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.width = width;
        this.height = height;
        this.map = new ListGraph<>();
        this.randomMapGenerator = new RandomMapGenerator(width, height);
        createMap();
    }

    public IGraph<Vector, Tile> getMap() {
        return map;
    }

    private void createMap() {
        Tile[][] grid = randomMapGenerator.getGrid();
        spawnPoint = randomMapGenerator.getStartTile();
        exitPoint = randomMapGenerator.getExitTile();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile currentTile = grid[x][y];

                // Add the current tile as a vertex in the graph
                map.addVertex(new Vector(x, y), currentTile);

                // Check neighboring tiles and add edges if they are passages
                if (x > 0 && grid[x - 1][y].getState() == TileState.PASSAGE) {
                    map.addEdge(new Vector(x, y), new Vector(x - 1, y), 1);
                }
                if (x < width - 1 && grid[x + 1][y].getState() == TileState.PASSAGE) {
                    map.addEdge(new Vector(x, y), new Vector(x + 1, y), 1);
                }
                if (y > 0 && grid[x][y - 1].getState() == TileState.PASSAGE) {
                    map.addEdge(new Vector(x, y), new Vector(x, y - 1), 1);
                }
                if (y < height - 1 && grid[x][y + 1].getState() == TileState.PASSAGE) {
                    map.addEdge(new Vector(x, y), new Vector(x, y + 1), 1);
                }
            }
        }

        for(Tile tile : map.getVertices()){
            tile.setCanvas(canvas);
            tile.setGc(gc);
        }
    }

    public void paint(){
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        spawnPoint.getGc().setFill(Color.BLUE);
        spawnPoint.getGc().fillRect(spawnPoint.getX() * 10, spawnPoint.getY() * 10, 10, 10);
        exitPoint.getGc().setFill(Color.RED);
        exitPoint.getGc().fillRect(spawnPoint.getX() * 10, spawnPoint.getY() * 10, 10, 10);
        for(Tile tile : map.getVertices()){
            if(!tile.equals(spawnPoint) && !tile.equals(exitPoint))
                tile.paint(this.canvas);
        }
    }
}
