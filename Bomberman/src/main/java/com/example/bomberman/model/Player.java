package com.example.bomberman.model;

import com.example.bomberman.collections.Vertex;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Player extends Character implements IExploitable{

    private Canvas canvas;
    private GraphicsContext graphicsContext;

    private Vector position;
    private ArrayList<Bomb> bombs;

    public Player(Canvas canvas) {
        super(canvas);
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.bombs = new ArrayList<>();
        this.idle = new ArrayList<>();
        this.run = new ArrayList<>();
        this.death = new ArrayList<>();

        for (int i = 0 ; i <= 3 ; i++){

        }
    }

    @Override
    public void move() {

    }

    @Override
    public void die() {

    }

    @Override
    public void paint() {

    }

    public Vector getPosition(){
        return position;

    }

    public void incrementBombCount() {
        this.bombs.add(new Bomb(this.canvas, 3, 1));
    }

    public void incrementBombRange() {
        this.bombs.forEach(Bomb::incrementRange);
    }

    public void incrementSpeed() {
        this.speed++;
    }
}
