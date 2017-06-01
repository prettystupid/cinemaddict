package com.prestu.gambler.games.snake;


import com.prestu.gambler.component.DifficultSelector;
import com.prestu.gambler.games.Game;
import com.prestu.gambler.games.GameDimension;
import com.prestu.gambler.utils.Notifications;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.UI;
import org.vaadin.hezamu.canvas.*;
import org.vaadin.hezamu.canvas.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Snake extends Game {

    private List<Point> snake;
    private int direction = 1;
    private Point fruit = null;

    private int pause_time_ms;
    private String BACKGROUND = "#0F0";
    private String SNAKE_COLOR = "#00F";
    private String HEAD_COLOR = "#F0F";


    @Override
    protected void initGame() {
        selector = new DifficultSelector() {
            @Override
            protected void easy() {
                dimension = new GameDimension(80, 60, 10);
                pause_time_ms = 500;
                difficultMultiplier = 1;
            }

            @Override
            protected void medium() {
                dimension = new GameDimension(80, 60, 10);
                pause_time_ms = 250;
                difficultMultiplier = 2;
            }

            @Override
            protected void hard() {
                dimension = new GameDimension(80, 60, 10);
                pause_time_ms = 100;
                difficultMultiplier = 4;
            }
        };
        setActionListeners();
        prepareGame();
    }

    @Override
    protected void prepareGame() {
        snake = new ArrayList<Point>();
        snake.add(new Point(1, 0));
        snake.add(new Point(2, 0));
        snake.add(new Point(3, 0));

        score = 0;
        gameOver = false;
        direction = 1;
        canvas.setWidth((dimension.getTileSize() * dimension.getWidth()) + "px");
        canvas.setHeight((dimension.getTileSize() * dimension.getHeight()) + "px");
        canvas.setFillStyle(BACKGROUND);
        canvas.fillRect(0, 0, dimension.getWidth() * dimension.getTileSize() + 2, dimension.getHeight() * dimension.getTileSize() + 2);
        generateFruit(snake.get(snake.size() - 1));
    }

    @Override
    protected void setActionListeners() {
        canvas.addShortcutListener(new ShortcutListener(null, null, ShortcutAction.KeyCode.ARROW_LEFT) {
            @Override
            public void handleAction(Object o, Object o1) {
                direction = (direction + 4 - 1 ) % 4;
            }
        });
        canvas.addShortcutListener(new ShortcutListener(null, null, ShortcutAction.KeyCode.ARROW_RIGHT) {
            @Override
            public void handleAction(Object o, Object o1) {
                direction = (direction + 4 + 1 ) % 4;
            }
        });
    }

    @Override
    protected synchronized void startGame() {
        Thread t = new Thread(() -> {
            while (running && !gameOver) {
                update();
                draw();
                try {
                    Thread.sleep(pause_time_ms);
                } catch (InterruptedException igmored) {}
            }
            endGame();
        });
        t.start();
    }

    @Override
    protected synchronized void update() {
        UI.getCurrent().access(() -> {
            Point head = snake.get(snake.size() - 1);
            Point newHead = null;
            switch (direction) {
                case 0:
                    newHead = new Point(head.x, head.y - 1);
                    break;
                case 1:
                    newHead = new Point(head.x + 1, head.y);
                    break;
                case 2:
                    newHead = new Point(head.x, head.y + 1);
                    break;
                case 3:
                    newHead = new Point(head.x - 1, head.y);
                    break;
            }

            if (newHead.x < 0 || newHead.x >= dimension.getWidth() ||
                    newHead.y < 0 || newHead.y >= dimension.getHeight()) {
                gameOver = true;
                return;
            }

            for (Point point : snake) {
                if (point.equals(newHead)) {
                    gameOver = true;
                    return;
                }
            }

            if (newHead.equals(fruit)) {
                generateFruit(newHead);
                score += 10 * difficultMultiplier;
                scoreLabel.setValue("Количество очков: " + score);
            } else {
                snake.remove(0);
            }
            snake.add(newHead);
        });
    }

    private void generateFruit(Point head) {
        boolean isNotOk = true;
        while (isNotOk) {
            fruit = new Point(
                    (int) (Math.random() * dimension.getWidth()),
                    (int) (Math.random() * dimension.getHeight())
            );
            if(head.equals(fruit)) {
                continue;
            }
            isNotOk = false;
            for (Point point : snake) {
                if(point.equals(fruit)) {
                    isNotOk = true;
                    break;
                }
            }
        }
    }

    private
    synchronized void draw() {
        UI.getCurrent().access(() -> {
            canvas.setFillStyle(BACKGROUND);
            int tileSize = dimension.getTileSize();
            canvas.fillRect(0, 0, dimension.getWidth() * tileSize + 2, dimension.getHeight() * tileSize + 2);

            canvas.setFillStyle(SNAKE_COLOR);
            for (Point point : snake) {
                canvas.fillRect(point.x * tileSize, point.y * tileSize, tileSize, tileSize);
            }
            Point head = snake.get(snake.size() - 1);
            canvas.setFillStyle(HEAD_COLOR);
            canvas.fillRect(head.x * tileSize, head.y * tileSize, tileSize, tileSize);

            canvas.setFillStyle("#F00");
            canvas.fillRect(fruit.x * tileSize, fruit.y * tileSize, tileSize, tileSize);
        });
    }

    @Override
    protected void endGame() {
        running = false;
        Notifications.show("Ваш счет " + score, "", Notifications.BOTTOM_PANEL);
        saveScore();
        startEndGame.setCaption("Начать");
        canvas.setFillStyle(BACKGROUND);
        canvas.fillRect(0, 0, dimension.getWidth() * dimension.getTileSize() + 2, dimension.getHeight() * dimension.getTileSize() + 2);
    }

    @Override
    public long getGameId() {
        return 2;
    }

    @Override
    public String getGameName() {
        return "Змейка";
    }
}
