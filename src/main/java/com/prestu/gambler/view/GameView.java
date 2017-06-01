package com.prestu.gambler.view;

import com.prestu.gambler.event.AppEventBus;
import com.prestu.gambler.games.Game;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public abstract class GameView extends CssLayout implements View {

    protected Game game;

    public GameView() {
        setSizeFull();
        addStyleName("game");
        AppEventBus.register(this);

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        tabs.addComponent(buildGameView());
        tabs.addComponent(buildRatingView());

        addComponent(tabs);
    }

    protected abstract Component buildGameView();

    protected abstract RatingView buildRatingView();

    @Override
    public void detach() {
        super.detach();
        AppEventBus.unregister(this);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
