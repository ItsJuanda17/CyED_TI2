package com.example.bomberman.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.*;

public class RandomMapGenerator {
    private int width;
    private int height;
    private Tile[][] grid;
    private Tile startTile;
    private Tile endTile;
    private Random random;

    public RandomMapGenerator(Canvas canvas, int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[width][height];
        this.random = new Random();
        generateMap(canvas);
        generateBreakableWalls();
        surroundByWalls();
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public Tile getStartTile() {
        return startTile;
    }

    public Tile getExitTile() {
        return endTile;
    }

    private Tile getRandomTile() {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        return grid[x][y];
    }

    private void generateBreakableWalls(){
        int breakableWalls = (int) (width * height * 0.2);
        for(int i = 0; i < breakableWalls; i++){
            Tile tile = getRandomTile();
            if(tile.getState() == TileState.BLOCKED){
                tile.setState(TileState.BREAKABLE_WALL);
            }else{
                i--;
            }
        }
    }

    private void surroundByWalls(){
        for(int i = 0; i < width; i++){
            grid[i][0].setState(TileState.BLOCKED);
            grid[i][height - 1].setState(TileState.BLOCKED);
        }
        for(int i = 0; i < height; i++){
            grid[0][i].setState(TileState.BLOCKED);
            grid[width - 1][i].setState(TileState.BLOCKED);
        }
    }

    private void generateMap(Canvas canvas) {
        // Start with a grid full of walls.
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height ; j++){
                grid[i][j] = new Tile(canvas,i, j);
                grid[i][j].setState(TileState.BLOCKED);
            }
        }

        // Pick a cell, set it to passage, and compute its frontier cells.
        int x = random.nextInt(width);
        int y = random.nextInt(height);

        grid[x][y].setState(TileState.PASSAGE);
        startTile = grid[x][y]; // set start tile

        // Frontier cell is a cell with distance 2 in state Blocked and within the grid.
        Set<Tile> frontierTiles = new HashSet<>(frontierTilesOf(grid[x][y]));

        // While the list of frontier cells is not empty:
        while (!frontierTiles.isEmpty()){
            // Pick a random frontier cell from the list of frontier cells
            Tile frontierTile = frontierTiles.stream().skip(random.nextInt(frontierTiles.size())).findFirst().orElse(null);

            //Get its neighbors: cells in distance 2 in state passage (no wall).
            List<Tile> frontierNeighbors = neighborsOf(frontierTile);

            if(!frontierNeighbors.isEmpty()) {
                Tile neighbor = frontierNeighbors.get(random.nextInt(frontierNeighbors.size()));
                //Set the cell in-between to Passage.
                connectInBetweenCells(frontierTile, neighbor);
            }

            //Compute the frontier cells of the chosen one and add them to the frontier list.
            frontierTiles.addAll(frontierTilesOf(frontierTile));
            //Remove the chosen frontier cell from the list of frontier cells.
            frontierTiles.remove(frontierTile);

            if(frontierTiles.isEmpty()){
                endTile = frontierTile; // set end tile as the last frontier tile
            }
        }
    }

    private List<Tile> frontierTilesOf(Tile tile) {
        return tilesAround(tile, TileState.BLOCKED);
    }

    private List<Tile> neighborsOf(Tile tile) {
        return tilesAround(tile, TileState.PASSAGE);
    }

    private List<Tile> tilesAround(Tile tile, TileState state) {
        List<Tile> frontier = new ArrayList<>();

        int x = tile.getX();
        int y = tile.getY();

        if (isValidPosition(x, y - 2) && grid[x][y - 2].getState() == state) {
            frontier.add(grid[x][y - 2]);
        }

        if (isValidPosition(x, y + 2) && grid[x][y + 2].getState() == state) {
            frontier.add(grid[x][y + 2]);
        }

        if (isValidPosition(x + 2, y) && grid[x + 2][y].getState() == state) {
            frontier.add(grid[x + 2][y]);
        }

        if (isValidPosition(x - 2, y) && grid[x - 2][y].getState() == state) {
            frontier.add(grid[x - 2][y]);
        }

        return frontier;
    }


    private void connectInBetweenCells(Tile tile, Tile neighbor) {
        int x = (tile.getX() + neighbor.getX()) / 2;
        int y = (tile.getY() + neighbor.getY()) / 2;
        tile.setState(TileState.PASSAGE);
        grid[x][y].setState(TileState.PASSAGE);
        neighbor.setState(TileState.PASSAGE);
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void printMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(grid[x][y].equals(startTile)) {
                    System.out.print("S");
                }else if(grid[x][y].equals(endTile)) {
                    System.out.print("E");
                }else {
                    System.out.print(grid[x][y].getState() == TileState.PASSAGE ? " " : grid[x][y].getState() == TileState.BREAKABLE_WALL ? "X" : "#");
                }
            }
            System.out.println();
        }
    }

    public void paint(){
        startTile.getGc().setFill(Color.GREEN);
        startTile.getGc().fillRect(startTile.getX() * 10, startTile.getY() * 10, 10, 10);
        endTile.getGc().setFill(Color.RED);
        endTile.getGc().fillRect(endTile.getX() * 10, endTile.getY() * 10, 10, 10);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(!grid[x][y].equals(startTile) && !grid[x][y].equals(endTile)) {
                    grid[x][y].getGc().setFill(grid[x][y].getState() == TileState.PASSAGE ? Color.WHITE : grid[x][y].getState() == TileState.BREAKABLE_WALL ? Color.GRAY : Color.BLACK);
                    grid[x][y].getGc().fillRect(x * 10, y * 10, 10, 10);
                }
            }
        }
    }

}


