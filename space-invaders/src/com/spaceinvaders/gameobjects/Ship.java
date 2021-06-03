package com.spaceinvaders.gameobjects;

import com.codegym.engine.cell.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameObject {
    public boolean isAlive = true;
    private List<int[][]> frames;
    private int frameIndex;
    private boolean loopAnimation;

    public Ship(double x, double y) {
        super(x, y);
    }

    public void setStaticView(int[][] viewFrame) {
        setMatrix(viewFrame);
        frames = new ArrayList<>();
        frames.add(viewFrame);
        frameIndex = 0;
    }

    public void setAnimatedView(boolean loopAnimation, int[][]... viewFrames) {
        setMatrix(viewFrames[0]);
        frames = Arrays.asList(viewFrames);
        frameIndex = 0;
        this.loopAnimation = loopAnimation;
    }

    public void nextFrame() {
        frameIndex += 1;
        if(frameIndex >= frames.size() && !loopAnimation) {
            return;
        } if(frameIndex >= frames.size() && loopAnimation) {
            frameIndex = 0;
        }
        matrix = frames.get(frameIndex);

    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        nextFrame();
    }

    public Bullet fire() {
        return null;
    }

    public void kill() {
        isAlive = false;
    }

    public boolean isVisible() {
        if(!isAlive && frameIndex >= frames.size()) {
            return false;
        } else {
            return true;
        }
    }
}
