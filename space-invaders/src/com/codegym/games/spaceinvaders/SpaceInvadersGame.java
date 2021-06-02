package com.codegym.games.spaceinvaders;

import com.codegym.engine.cell.*;
import com.codegym.games.spaceinvaders.gameobjects.Bullet;
import com.codegym.games.spaceinvaders.gameobjects.EnemyFleet;
import com.codegym.games.spaceinvaders.gameobjects.PlayerShip;
import com.codegym.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int DIFFICULTY = 5;
    private static final int PLAYER_BULLETS_MAX = 1;

    private List<Star> stars;
    private EnemyFleet enemyFleet;
    private List<Bullet> enemyBullets;

    private PlayerShip playerShip;
    private List<Bullet> playerBullets;

    private boolean isGameStopped = false;
    private int animationsCount;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        isGameStopped = false;
        animationsCount = 0;
        score = 0;

        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        playerBullets = new ArrayList<>();

        createStars();
        drawScene();
        setTurnTimer(40);
    }

    private void drawScene() {
        drawField();
        enemyFleet.draw(this);
        playerShip.draw(this);

        for (Bullet bullet : enemyBullets) {
            bullet.draw(this);
        }
        for (Bullet playerBullet : playerBullets) {
            playerBullet.draw(this);
        }

    }

    private void drawField() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }
        for (Star star : stars) {
            star.draw(this);
        }
    }

    private void createStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = getRandomNumber(WIDTH);
            int y = getRandomNumber(HEIGHT);
            stars.add(new Star(x, y));
        }
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        playerShip.move();
        for (Bullet bullet : enemyBullets) {
            bullet.move();
        }
        for (Bullet playerBullet : playerBullets) {
            playerBullet.move();
        }
    }

    private void removeDeadBullets() {
        enemyBullets.removeIf(bullet -> !bullet.isAlive || bullet.y >= HEIGHT - 1);
        playerBullets.removeIf(bullet -> !bullet.isAlive || bullet.y + bullet.height < 0);
    }

    private void check() {
        playerShip.checkHit(enemyBullets);

        score += enemyFleet.checkHit(playerBullets);

        enemyFleet.deleteHiddenShips();
        removeDeadBullets();

        if (!playerShip.isAlive) {
            stopGameWithDelay();
        }

        if (enemyFleet.getBottomBorder() >= playerShip.y) {
            playerShip.kill();
        }


        if (enemyFleet.getShipCount() == 0) {
            playerShip.win();
            stopGameWithDelay();
        }
    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        check();
        Bullet projectile = enemyFleet.fire(this);
        if (projectile != null) {
            enemyBullets.add(projectile);
        }
        setScore(score);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (key.equals(Key.SPACE) && isGameStopped) {
            createGame();
            return;
        }
        if (key.equals(Key.LEFT)) {
            playerShip.setDirection(Direction.LEFT);
        }
        if (key.equals(Key.RIGHT)) {
            playerShip.setDirection(Direction.RIGHT);
        }
        if (key.equals(Key.SPACE)) {
            Bullet playerProjectile = playerShip.fire();
            if (playerProjectile != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                playerBullets.add(playerProjectile);
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key.equals(Key.LEFT) && playerShip.getDirection().equals(Direction.LEFT)) {
            playerShip.setDirection(Direction.UP);
        }
        if (key.equals(Key.RIGHT) && playerShip.getDirection().equals(Direction.RIGHT)) {
            playerShip.setDirection(Direction.UP);
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if (x >= WIDTH || y >= HEIGHT || x < 0 || y < 0) {
            return;
        }
        super.setCellValueEx(x, y, cellColor, value);
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.NONE, "You win! Congratulations!", Color.GREEN, 36);
        } else {
            showMessageDialog(Color.NONE, "You lost :( try again!", Color.RED, 36);
        }
    }

    private void stopGameWithDelay() {
        animationsCount += 1;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }
}
