package com.prestu.cinemaddict.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.prestu.cinemaddict.CinemaddictAppUI;

public class AppEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        CinemaddictAppUI.getDashboardEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        CinemaddictAppUI.getDashboardEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        CinemaddictAppUI.getDashboardEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
            final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
