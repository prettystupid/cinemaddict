package com.prestu.cinemaddict.view;

import com.prestu.cinemaddict.CinemaddictNavigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class MainView extends HorizontalLayout {

    public MainView() {
        setSizeFull();
        addStyleName("mainview");

        addComponent(new MainMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new CinemaddictNavigator(content);
    }
}
