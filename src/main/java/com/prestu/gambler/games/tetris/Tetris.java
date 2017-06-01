package com.prestu.gambler.games.tetris;

import com.prestu.gambler.component.DifficultSelector;
import com.prestu.gambler.games.Game;
import com.prestu.gambler.games.GameDimension;
import com.prestu.gambler.utils.Notifications;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.UI;

public class Tetris extends Game {

    private int pause_time_ms;

    private Grid playfield;
    private static final String PLAYFIELD_COLOR = "#000";

    private Tetromino activeTetrimino;
    private int pos_x, pos_y;

    @Override
    protected void initGame() {
        selector = new DifficultSelector() {
            @Override
            protected void easy() {
                dimension = new GameDimension(10, 20, 30);
                pause_time_ms = 500;
                difficultMultiplier = 1;
            }

            @Override
            protected void medium() {
                dimension = new GameDimension(15, 20, 30);
                pause_time_ms = 250;
                difficultMultiplier = 2;
            }

            @Override
            protected void hard() {
                dimension = new GameDimension(20, 20, 30);
                pause_time_ms = 150;
                difficultMultiplier = 4;
            }
        };
        canvas.setWidth((dimension.getTileSize() * dimension.getWidth()) + "px");
        canvas.setHeight((dimension.getTileSize() * dimension.getHeight()) + "px");
        canvas.setFillStyle(PLAYFIELD_COLOR);
        canvas.fillRect(0, 0, dimension.getWidth() * dimension.getTileSize() + 2, dimension.getHeight() * dimension.getTileSize() + 2);
        setActionListeners();
    }

    @Override
    protected void prepareGame() {
        playfield = new Grid(dimension.getWidth(), dimension.getHeight());
        canvas.setWidth((dimension.getTileSize() * dimension.getWidth()) + "px");
        canvas.setHeight((dimension.getTileSize() * dimension.getHeight()) + "px");
        score = 0;
        gameOver = false;
        activeTetrimino = Tetromino.getRandom();
        pos_y = 0;
        pos_x = (dimension.getWidth() - activeTetrimino.getWidth()) / 2;
        clear();
    }

    @Override
    protected void setActionListeners() {
        canvas.addShortcutListener(new ShortcutListener(null, null, ShortcutAction.KeyCode.ARROW_LEFT) {
            @Override
            public void handleAction(Object o, Object o1) {
                moveLeft();
                update();
            }
        });
        canvas.addShortcutListener(new ShortcutListener(null, null, ShortcutAction.KeyCode.ARROW_RIGHT) {
            @Override
            public void handleAction(Object o, Object o1) {
                moveRight();
                update();
            }
        });
        canvas.addShortcutListener(new ShortcutListener(null, null, ShortcutAction.KeyCode.ARROW_DOWN) {
            @Override
            public void handleAction(Object o, Object o1) {
                drop();
                update();
            }
        });
        canvas.addShortcutListener(new ShortcutListener(null, null, ShortcutAction.KeyCode.ARROW_UP) {
            @Override
            public void handleAction(Object o, Object o1) {
                rotateCW();
                update();
            }
        });
    }

    @Override
    protected synchronized void startGame() {
        Thread t = new Thread(() -> {
            while (running && !gameOver) {
                update();
                try {
                    Thread.sleep(pause_time_ms);
                } catch (InterruptedException igmored) {
                }
                step();

            }
            update();
            endGame();
        });
        t.start();
    }

    @Override
    protected synchronized void update() {
        UI.getCurrent().access(() -> {
            canvas.clear();
            canvas.setFillStyle(PLAYFIELD_COLOR);
            canvas.fillRect(0, 0, dimension.getWidth() * dimension.getTileSize() + 2, dimension.getHeight() * dimension.getTileSize() + 2);

            Grid state = getCurrentState();
            for (int x = 0; x < state.getWidth(); x++) {
                for (int y = 0; y < state.getHeight(); y++) {

                    int tile = state.get(x, y);
                    if (tile > 0) {

                        String color = Tetromino.get(tile).getColor();
                        canvas.setFillStyle(color);
                        canvas.fillRect(x * dimension.getTileSize() + 1, y * dimension.getTileSize() + 1,
                                dimension.getTileSize() - 2, dimension.getTileSize() - 2);
                    }
                }
            }

            scoreLabel.setValue("Количество очков: " + score);
        });
    }

    @Override
    protected void endGame() {
        running = false;
        Notifications.show("Ваш счет " + score, "", Notifications.BOTTOM_PANEL);
        saveScore();
        startEndGame.setCaption("Начать");
        canvas.setFillStyle(PLAYFIELD_COLOR);
        canvas.fillRect(0, 0, dimension.getWidth() * dimension.getTileSize() + 2, dimension.getHeight() * dimension.getTileSize() + 2);
    }

    @Override
    public long getGameId() {
        return 1;
    }

    @Override
    public String getGameName() {
        return "Тетрис";
    }

    private void clear() {
        playfield.fill(0, 0, playfield.getWidth(), playfield.getHeight(), 0);
    }

    public Grid getCurrentState() {
        Grid state = new Grid(playfield);
        state.copy(activeTetrimino, pos_x, pos_y);
        return state;
    }

    private boolean step() {
        if (!playfield.fitsInto(activeTetrimino, pos_x, pos_y + 1)) {

            playfield.copy(activeTetrimino, pos_x, pos_y);

            for (int j = playfield.getHeight() - 1; j >= 0; j--)
                while (isFullLine(j) == true) {
                    clearLine(j);
                    score += 10*difficultMultiplier;
                }

            activeTetrimino = Tetromino.getRandom();
            pos_y = 0;
            pos_x = (playfield.getWidth() - activeTetrimino.getWidth()) / 2;
            if (!playfield.fitsInto(activeTetrimino, pos_x, pos_y))
                this.gameOver = true;
        } else {
            pos_y++;
        }
        return gameOver;
    }

    private boolean isFullLine(int y) {
        for (int x = 0; x < playfield.getWidth(); x++)
            if (playfield.isEmpty(x, y))
                return false;
        return true;
    }

    private void clearLine(int y) {
        for (int j = y; j > 0; j--)
            for (int i = 0; i < playfield.getWidth(); i++)
                playfield.set(i, j, playfield.get(i, j - 1));
        for (int i = 0; i < playfield.getWidth(); i++)
            playfield.set(i, 0, 0);
    }

    private void moveLeft() {
        if (!playfield.fitsInto(activeTetrimino, pos_x - 1, pos_y))
            return;
        pos_x--;
    }

    private void moveRight() {
        if (!playfield.fitsInto(activeTetrimino, pos_x + 1, pos_y))
            return;
        pos_x++;
    }

    private void rotateCW() {
        Tetromino test = new Tetromino(activeTetrimino);
        test.rotateCW();
        if (playfield.fitsInto(test, pos_x, pos_y))
            activeTetrimino = test;
    }

    private void drop() {
        while (playfield.fitsInto(activeTetrimino, pos_x, pos_y + 1))
            pos_y++;
    }
}
