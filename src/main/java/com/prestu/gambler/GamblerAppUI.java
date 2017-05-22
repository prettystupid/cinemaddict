package com.prestu.gambler;

import javax.servlet.annotation.WebServlet;

import com.google.common.eventbus.Subscribe;
import com.prestu.gambler.data.DataProvider;
import com.prestu.gambler.data.dummy.DummyDataProvider;
import com.prestu.gambler.domain.User;
import com.prestu.gambler.event.AppEvent;
import com.prestu.gambler.event.AppEventBus;
import com.prestu.gambler.view.LoginView;
import com.prestu.gambler.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@Theme("gambler")
public class GamblerAppUI extends UI {

    private final DataProvider dataProvider = new DummyDataProvider();
    private final AppEventBus appEventbus = new AppEventBus();

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        AppEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(
                new Page.BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final Page.BrowserWindowResizeEvent event) {
                        AppEventBus.post(new AppEvent.BrowserResizeEvent());
                    }
                });
    }

    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @WebServlet(urlPatterns = "/*", name = "ApplicationServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = GamblerAppUI.class, productionMode = false)
    public static class ApplicationServlet extends VaadinServlet {
    }

    @Subscribe
    public void userLoginRequested(final AppEvent.UserLoginRequestedEvent event) {
        User user = getDataProvider().authenticate(event.getUserName(),
                event.getPassword());
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final AppEvent.UserLoggedOutEvent event) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final AppEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    public static DataProvider getDataProvider() {
        return ((GamblerAppUI) getCurrent()).dataProvider;
    }

    public static AppEventBus getGamblerEventbus() {
        return ((GamblerAppUI) getCurrent()).appEventbus;
    }
}
