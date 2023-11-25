package com.example.bomberman;

import com.example.bomberman.model.RandomMapGenerator;
import com.example.bomberman.model.Tile;
import com.example.bomberman.model.TileState;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RandomMapGeneratorTest {

    private RandomMapGenerator randomMapGenerator;

    @BeforeEach
    public void setup1() {
        randomMapGenerator = new RandomMapGenerator(new Canvas(), 10, 10);
    }

    @Test
    public void testGeneratedMapNotNull() {
        assertNotNull(randomMapGenerator.getGrid());
    }

    @Test
    public void testGeneratedMapSize() {
        Tile[][] grid = randomMapGenerator.getGrid();
        assertEquals(10, grid.length);
        assertEquals(10, grid[0].length);
    }

    @Test
    public void testGeneratedMapBoundaries() {
        Tile[][] grid = randomMapGenerator.getGrid();
        assertEquals(TileState.BLOCKED, grid[0][0].getState());
        assertEquals(TileState.BLOCKED, grid[9][0].getState());
        assertEquals(TileState.BLOCKED, grid[0][9].getState());
        assertEquals(TileState.BLOCKED, grid[9][9].getState());
    }

}