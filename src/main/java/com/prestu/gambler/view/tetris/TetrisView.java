package com.prestu.gambler.view.tetris;

import com.prestu.gambler.games.tetris.Tetris;
import com.prestu.gambler.view.GameView;
import com.prestu.gambler.view.RatingView;
import com.vaadin.ui.Component;


public class TetrisView extends GameView {

    @Override
    protected RatingView buildRatingView() {
        return new RatingView(game.getGameName(), game.getGameId());
    }

    @Override
    protected Component buildGameView() {
        game = new Tetris();
        return game;
    }


}
