package com.prestu.gambler.view;

import com.prestu.gambler.view.snake.SnakeView;
import com.prestu.gambler.view.tetris.TetrisView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

public enum ViewType {

    TETRIS("tetris", "Тетрис", TetrisView.class, new ThemeResource("img/tetris.png")),
    SNAKE("snake", "Змейка", SnakeView.class, new ThemeResource("img/snake.png"));

    private final String viewName;
    private final String caption;
    private final Class<? extends View> viewClass;
    private final Resource icon;

    ViewType(String viewName, String caption, Class<? extends View> viewClass, Resource icon) {
        this.caption = caption;
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
    }

    public String getViewName() {
        return viewName;
    }

    public String getCaption() {
        return caption;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static ViewType getByViewName(String viewName) {
        ViewType result = null;
        for (ViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
