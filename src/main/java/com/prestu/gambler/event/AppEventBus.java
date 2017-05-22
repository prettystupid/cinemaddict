package com.prestu.gambler.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.prestu.gambler.GamblerAppUI;

public class AppEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        GamblerAppUI.getGamblerEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        GamblerAppUI.getGamblerEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        GamblerAppUI.getGamblerEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
            final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
