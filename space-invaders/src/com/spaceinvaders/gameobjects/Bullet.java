package com.spaceinvaders.gameobjects;

import com.spaceinvaders.Direction;
import com.spaceinvaders.ShapeMatrix;

public class Bullet extends GameObject {
    private int dy;
    public boolean isAlive = true;

    public Bullet(double x, double y, Direction direction) {
        super(x, y);
        setMatrix(ShapeMatrix.BULLET);
        if(direction.equals(Direction.UP)) {
            dy = -1;
        } else if(!direction.equals(Direction.UP)) {
            dy = 1;
        }
    }

    public void move() {
        y += dy;
    }

    public void kill() {
        isAlive = false;
    }
}
