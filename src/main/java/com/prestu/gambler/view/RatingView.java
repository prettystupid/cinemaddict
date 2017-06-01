package com.prestu.gambler.view;

import com.prestu.gambler.GamblerAppUI;
import com.prestu.gambler.domain.ScoreInfo;
import com.prestu.gambler.event.AppEventBus;
import com.vaadin.data.Container;
import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ItemSorter;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class RatingView extends VerticalLayout {

    private String gameName;
    private long gameId;

    private Table table;

    public RatingView(String gameName, long gameId) {
        setSizeFull();
        setCaption("Рейтинг");
        addStyleName("rating");
        AppEventBus.register(this);

        this.gameName = gameName;
        this.gameId = gameId;

        addComponent(buildToolbar(gameName));

        table = buildTable();
        refresh();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    private Component buildToolbar(String gameName) {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        header.setMargin(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Рейтинг игры " + gameName);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        return header;
    }

    private Table buildTable() {
        Table table = new Table();
        table.setSizeFull();
        table.addContainerProperty("username", String.class, "", "Пользователь", null, null);
        table.addContainerProperty("score", Integer.class, "", "Счет", null, null);
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);
        table.setColumnExpandRatio("username", 1);
        table.setColumnExpandRatio("score", 1);
        ItemSorter sorter = new ItemSorter() {
            @Override
            public void setSortProperties(Container.Sortable container, Object[] propertyId, boolean[] ascending) {

            }

            @Override
            public int compare(Object itemId1, Object itemId2) {
                try {
                    int score1 = (Integer) itemId1;
                    int score2 = (Integer) itemId2;
                    return Integer.signum(score2 - score1);
                } catch (ClassCastException e) {
                    return 0;
                }
            }
        };
        Container container = table.getContainerDataSource();
        if (container instanceof IndexedContainer) {
            ((IndexedContainer) container).setItemSorter(sorter);
        } else if (container instanceof AbstractBeanContainer){
            ((AbstractBeanContainer) container).setItemSorter(sorter);
        }
        table.setImmediate(true);


        return table;
    }

    public void refresh() {
        for (ScoreInfo scoreInfo: GamblerAppUI.getDataProvider().getScoreInfosForGame(gameId, gameName)) {
            table.addItem(new Object[] {scoreInfo.getUsername(), scoreInfo.getScore()}, scoreInfo.getScore());
        }
        table.sort(new Object[] {"descriprtion"}, new boolean[] {true});
    }
}
