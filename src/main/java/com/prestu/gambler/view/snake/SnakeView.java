package com.prestu.gambler.view.snake;

import com.prestu.gambler.games.snake.Snake;
import com.prestu.gambler.view.GameView;
import com.prestu.gambler.view.RatingView;
import com.vaadin.ui.Component;

public class SnakeView extends GameView {


    @Override
    protected Component buildGameView() {
        game = new Snake();
        return game;
    }

    @Override
    protected RatingView buildRatingView() {
        return new RatingView(game.getGameName(), game.getGameId());
    }
}
