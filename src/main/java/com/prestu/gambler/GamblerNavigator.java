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
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

public class GamblerNavigator extends Navigator {

    private static final String TRACKER_ID = null;
    private GoogleAnalyticsTracker tracker;

    private static final ViewType ERROR_VIEW = ViewType.TRANSACTIONS;
    private ViewProvider errorViewProvider;

    public GamblerNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);

        String host = getUI().getPage().getLocation().getHost();
        if (TRACKER_ID != null && host.endsWith("demo.vaadin.com")) {
            initGATracker(TRACKER_ID);
        }
        initViewChangeListener();
        initViewProviders();

    }

    private void initGATracker(final String trackerId) {
        tracker = new GoogleAnalyticsTracker(trackerId, "demo.vaadin.com");

        tracker.extend(UI.getCurrent());
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                ViewType view = ViewType.getByViewName(event
                        .getViewName());
                AppEventBus.post(new AppEvent.PostViewChangeEvent(view));
                AppEventBus.post(new AppEvent.BrowserResizeEvent());
                AppEventBus.post(new AppEvent.CloseOpenWindowsEvent());

                if (tracker != null) {
                    tracker.trackPageview("/gambler/" + event.getViewName());
                }
            }
        });
    }

    private void initViewProviders() {
        for (final ViewType viewType : ViewType.values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(
                    viewType.getViewName(), viewType.getViewClass()) {

                private View cachedInstance;

                @Override
                public View getView(final String viewName) {
                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        if (viewType.isStateful()) {
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewType
                                        .getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            result = super.getView(viewType.getViewName());
                        }
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
            public String getViewName(final String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }
}
