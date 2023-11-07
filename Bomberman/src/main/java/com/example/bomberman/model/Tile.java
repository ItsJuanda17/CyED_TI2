package com.example.bomberman.model;

public class Tile {

    private int x;
    private int y;
    private boolean isPassable;
    private boolean hasBomb;


    public Tile(int x, int y, boolean isPassable) {
        this.x = x;
        this.y = y;
        this.isPassable = isPassable;
        this.hasBomb = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    public void placeBomb() {
        hasBomb = true;
    }

    public void removeBomb() {
        hasBomb = false;
    }
}
