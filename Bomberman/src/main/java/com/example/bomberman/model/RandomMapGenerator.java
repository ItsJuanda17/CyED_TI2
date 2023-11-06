package com.example.bomberman.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMapGenerator {
    private int width;
    private int height;
    private Tile[][] grid;
    private Tile startTile;
    private Tile endTile;
    private Random random;

    public RandomMapGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[width][height];
        this.random = new Random();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Tile(x, y);
            }
        }
    }

    public Tile[][] generateMap() {
        startTile = getRandomTile();
        startTile.setState(TileState.PASSAGE);

        List<Tile> frontierTiles = new ArrayList<>();
        addFrontierTiles(startTile, frontierTiles);

        while (!frontierTiles.isEmpty()) {
            Tile frontierTile = getRandomFrontierTile(frontierTiles);
            List<Tile> neighbors = getNeighbors(frontierTile);

            if (!neighbors.isEmpty()) {
                Tile randomNeighbor = getRandomNeighbor(neighbors);
                if (randomNeighbor.getState() == TileState.BLOCKED) {
                    randomNeighbor.setState(TileState.PASSAGE);
                    Tile inBetweenTile = grid[(frontierTile.getX() + randomNeighbor.getX()) / 2][(frontierTile.getY() + randomNeighbor.getY()) / 2];
                    inBetweenTile.setState(TileState.PASSAGE);
                    addFrontierTiles(randomNeighbor, frontierTiles);
                }
            }

            double distance;
            do {
                endTile = getRandomTile();
                distance = Math.abs(endTile.getX() - startTile.getX()) + Math.abs(endTile.getY() - startTile.getY());
            } while (distance < (double) width / 2 || distance > width * 2 || endTile.getState() == TileState.PASSAGE);


            frontierTiles.remove(frontierTile);
        }

        return this.grid;
    }

    private Tile getRandomTile() {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        return grid[x][y];
    }

    private Tile getRandomFrontierTile(List<Tile> frontierTiles) {
        int index = random.nextInt(frontierTiles.size());
        return frontierTiles.get(index);
    }

    private List<Tile> getNeighbors(Tile tile) {
        List<Tile> neighbors = new ArrayList<>();
        int x = tile.getX();
        int y = tile.getY();

        if (x >= 2) neighbors.add(grid[x - 2][y]);
        if (x + 2 < width) neighbors.add(grid[x + 2][y]);
        if (y >= 2) neighbors.add(grid[x][y - 2]);
        if (y + 2 < height) neighbors.add(grid[x][y + 2]);

        return neighbors;
    }

    private Tile getRandomNeighbor(List<Tile> neighbors) {
        int index = random.nextInt(neighbors.size());
        return neighbors.get(index);
    }

    private void addFrontierTiles(Tile tile, List<Tile> frontierTiles) {
        int x = tile.getX();
        int y = tile.getY();

        if (x >= 2) frontierTiles.add(grid[x - 2][y]);
        if (x + 2 < width) frontierTiles.add(grid[x + 2][y]);
        if (y >= 2) frontierTiles.add(grid[x][y - 2]);
        if (y + 2 < height) frontierTiles.add(grid[x][y + 2]);
    }

    public Tile getStartTile() {
        return startTile;
    }

    public Tile getExitTile() {
        return endTile;
    }
}
