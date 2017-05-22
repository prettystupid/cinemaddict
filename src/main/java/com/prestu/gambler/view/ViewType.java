package com.prestu.gambler.view;

import com.prestu.gambler.view.schedule.ScheduleView;
import com.prestu.gambler.view.transactions.TransactionsView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum ViewType {

    SCHEDULE("schedule", "Мои просмотры", ScheduleView.class, FontAwesome.CALENDAR_O, false),
    TRANSACTIONS("transactions", "Сеансы", TransactionsView.class, FontAwesome.TABLE, false);

    private final String viewName;
    private final String caption;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    ViewType(final String viewName, final String caption,
                     final Class<? extends View> viewClass, final Resource icon,
                     final boolean stateful) {
        this.caption = caption;
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
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

    public static ViewType getByViewName(final String viewName) {
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
