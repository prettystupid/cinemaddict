package com.prestu.gambler.games;

import com.prestu.gambler.component.DifficultSelector;
import com.prestu.gambler.event.AppEvent;
import com.prestu.gambler.event.AppEventBus;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.hezamu.canvas.Canvas;

public abstract class Game extends VerticalLayout {

    protected Canvas canvas;
    protected Button startEndGame;
    protected DifficultSelector selector;
    protected Label scoreLabel;

    protected int score = 0;
    protected int difficultMultiplier = 1;
    protected boolean running;
    protected boolean gameOver;
    protected GameDimension dimension;

    public Game() {
        super();
        addStyleName(ValoTheme.LAYOUT_WELL);
        setMargin(true);
        setSpacing(true);

        scoreLabel = new Label("Количество очков: ");

        canvas = new Canvas();

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        startEndGame = new Button("Начать");
        startEndGame.addStyleName(ValoTheme.BUTTON_PRIMARY);
        startEndGame.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (!running) {
                    startEndGame.setCaption("Закончить");
                    running = true;
                    prepareGame();
                    startGame();
                } else {
                    startEndGame.setCaption("Начать");
                    running = false;
                    endGame();
                }
            }
        });

        setCaption(getGameName());
        initGame();

        buttons.addComponents(startEndGame, selector);

        addComponents(scoreLabel, canvas, buttons);

        setComponentAlignment(canvas, Alignment.MIDDLE_CENTER);
        setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

    }

    public int getScore(){
        return score;
    }

    protected abstract void initGame();

    protected abstract void prepareGame();

    protected abstract void setActionListeners();

    protected abstract void startGame();

    protected abstract void update();

    protected abstract void endGame();

    protected void saveScore() {
        AppEventBus.post(new AppEvent.EndGameEvent(getGameId(), getScore()));
    }

    public abstract long getGameId();

    public abstract String getGameName();
}
