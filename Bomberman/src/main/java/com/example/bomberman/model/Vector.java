package com.example.bomberman.model;

public class Vector {

    private double posX;
    private double posY;

    public Vector(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void normalize(){
        double normal = Math.sqrt(Math.pow(posX, 2) + Math.pow(posY, 2));
        if(normal != 0){
            posX /= normal;
            posY /= normal;
        }
    }

    public void setSpeed(int speed){
        posX *= speed;
        posY *= speed;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }
}
