package com.example.bomberman.model;

public class Vector {

    private int posX;
    private int posY;

    public Vector(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void normalize(){
        double normal = Math.sqrt(Math.pow(posX, 2) + Math.pow(posY, 2));
        if(normal != 0){
            posX /= (int) normal;
            posY /= (int) normal;
        }
    }

    public void setSpeed(int speed){
        posX *= speed;
        posY *= speed;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
