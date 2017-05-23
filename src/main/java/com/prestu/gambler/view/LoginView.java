package com.prestu.gambler.view;

import com.prestu.gambler.event.AppEvent;
import com.prestu.gambler.event.AppEventBus;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    private Component loginForm;
    private Component registrationForm;
    private TextField username;
    private PasswordField password;

    public LoginView() {
        setSizeFull();

        loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(buildButtons());
        return loginPanel;
    }

    private Component buildRegistrationForm() {
        VerticalLayout registerPanel = new VerticalLayout();
        registerPanel.setSizeUndefined();
        registerPanel.setSpacing(true);
        Responsive.makeResponsive(registerPanel);
        registerPanel.addStyleName("login-panel");

        registerPanel.addComponent(buildLabels());
        registerPanel.addComponent(buildRegisterFields());
        return registerPanel;
    }

    private Component buildRegisterFields() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        TextField username = new TextField("Логин");

        username.addValidator(new Validator() {
            @Override
            public void validate(Object o) throws InvalidValueException {
                String user = (String) o;
                if (user == null || user.isEmpty()) throw new InvalidValueException("Введите логин");
            }
        });

        TextField firstName = new TextField("Имя");

        TextField lastName = new TextField("Фамилия");

        TextField email = new TextField("E-mail");

        PasswordField password = new PasswordField("Пароль");
        password.addValidator(new Validator() {
            @Override
            public void validate(Object o) throws InvalidValueException {
                String pass = (String) o;
                if (pass == null || pass.isEmpty() || pass.length() < 8) throw new InvalidValueException("Пароль должен содержать минимум 8 символов");
            }
        });

        Button register = new Button("Создать аккаунт");
        register.addStyleName(ValoTheme.BUTTON_PRIMARY);
        register.setEnabled(username.isValid() && password.isValid());
        register.setClickShortcut(KeyCode.ENTER);
        register.focus();

        Property.ValueChangeListener validUserPassListener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                register.setEnabled(username.isValid() && password.isValid());
            }
        };

        username.addValueChangeListener(validUserPassListener);
        password.addValueChangeListener(validUserPassListener);
        register.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {

                // TODO: 23.05.2017  
//                AppEventBus.post(new AppEvent.UserLoginRequestedEvent(username
//                        .getValue(), password.hashCode()));
                Notification notification = new Notification("Добро пожаловать на GAMBLER");
                notification.setDescription("<span>Регистрация прошла успешно</span>");
                notification.setHtmlContentAllowed(true);
                notification.setStyleName("tray dark small closable login-help");
                notification.setPosition(Position.BOTTOM_CENTER);
                notification.setDelayMsec(20000);
                notification.show(Page.getCurrent());
                popupLoginForm();
            }
        });

        fields.addComponents(username, firstName, lastName, email, password, register);
        return fields;
    }

    private void popupRegistrationForm() {
        if (registrationForm == null) registrationForm = buildRegistrationForm();
        replaceComponent(loginForm, registrationForm);
    }

    private void popupLoginForm() {
        replaceComponent(registrationForm, loginForm);
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);

        username = new TextField("Логин");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password = new PasswordField("Пароль");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        fields.addComponents(username, password);
        return fields;
    }

    private Component buildButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button signin = new Button("Войти");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        Button signup = new Button("Регистрация");
        signup.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signup.setClickShortcut(KeyCode.ENTER);

        buttons.addComponents(signin, signup);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                AppEventBus.post(new AppEvent.UserLoginRequestedEvent(username
                        .getValue(), password.hashCode()));
            }
        });
        signup.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                popupRegistrationForm();
            }
        });
        return buttons;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Добро пожаловать");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("GAMBLER");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        labels.addComponent(title);
        return labels;
    }

}
