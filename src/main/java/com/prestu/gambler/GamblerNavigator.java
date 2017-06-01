package com.prestu.gambler;

import com.prestu.gambler.event.AppEvent;
import com.prestu.gambler.event.AppEventBus;
import com.prestu.gambler.view.ViewType;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

public class GamblerNavigator extends Navigator {

    private static final ViewType ERROR_VIEW = ViewType.TETRIS;
    private ViewProvider errorViewProvider;

    public GamblerNavigator(ComponentContainer container) {
        super(UI.getCurrent(), container);

        initViewChangeListener();
        initViewProviders();

    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                ViewType view = ViewType.getByViewName(event.getViewName());
                AppEventBus.post(new AppEvent.PostViewChangeEvent(view));
                AppEventBus.post(new AppEvent.BrowserResizeEvent());
                AppEventBus.post(new AppEvent.CloseOpenWindowsEvent());
            }
        });
    }

    private void initViewProviders() {
        for (ViewType viewType : ViewType.values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(viewType.getViewName(), viewType.getViewClass()) {

                @Override
                public View getView(String viewName) {
                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        result = super.getView(viewType.getViewName());
                    }
                    return result;
                }
            };

            if (viewType == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }

            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }
}
