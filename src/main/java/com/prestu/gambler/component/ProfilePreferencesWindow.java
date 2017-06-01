package com.prestu.gambler.component;

import com.prestu.gambler.domain.User;
import com.prestu.gambler.event.AppEvent;
import com.prestu.gambler.event.AppEventBus;
import com.prestu.gambler.utils.Notifications;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.*;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class ProfilePreferencesWindow extends Window {

    private BeanFieldGroup<User> fieldGroup;
    @PropertyId("firstName")
    private TextField firstNameField;
    @PropertyId("lastName")
    private TextField lastNameField;
    @PropertyId("male")
    private OptionGroup sexField;
    @PropertyId("email")
    private TextField emailField;
    @PropertyId("city")
    private TextField locationField;
    @PropertyId("website")
    private TextField websiteField;
    @PropertyId("bio")
    private TextArea bioField;
    private Image profilePic;

    private User user;

    private ProfilePreferencesWindow(User user) {
        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        this.user = user;
        setModal(true);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(true);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildProfileTab());

        content.addComponent(buildFooter());

        fieldGroup = new BeanFieldGroup<>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
    }


    private Component buildProfileTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Профиль");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        profilePic = new Image();
        setProfilePic(user.getLogo());
        pic.addComponent(profilePic);

        Button upload = new Button("Изменить…", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                popupLogoSelectWindow();
            }
        });
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pic.addComponent(upload);

        root.addComponent(pic);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        firstNameField = new TextField("Имя");
        details.addComponent(firstNameField);
        lastNameField = new TextField("Фамилия");
        details.addComponent(lastNameField);

        sexField = new OptionGroup("Пол");
        sexField.addItem(Boolean.FALSE);
        sexField.setItemCaption(Boolean.TRUE, "Мужской");
        sexField.addItem(Boolean.TRUE);
        sexField.setItemCaption(Boolean.FALSE, "Женский");
        sexField.addStyleName("horizontal");
        details.addComponent(sexField);

        Label section = new Label("Контактная информация");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.setNullRepresentation("");
        details.addComponent(emailField);

        locationField = new TextField("Город");
        locationField.setWidth("100%");
        locationField.setNullRepresentation("");
        details.addComponent(locationField);

        section = new Label("Другая информация");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        websiteField = new TextField("Сайт");
        websiteField.setInputPrompt("https://");
        websiteField.setWidth("100%");
        websiteField.setNullRepresentation("");
        details.addComponent(websiteField);

        bioField = new TextArea("О себе");
        bioField.setWidth("100%");
        bioField.setRows(4);
        bioField.setNullRepresentation("");
        details.addComponent(bioField);

        return root;
    }

    private void setProfilePic(ThemeResource resource) {
        profilePic.setSource(resource);
        profilePic.setWidth(100.0f, Unit.PIXELS);
    }

    private void popupLogoSelectWindow() {
        Window w = new LogoSelectWindow();
        UI.getCurrent().addWindow(w);
        w.focus();
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                    Notifications.show("Профиль успешно обновлен", "", Notifications.BOTTOM_PANEL);
                    AppEventBus.post(new AppEvent.ProfileUpdatedEvent(user));
                    close();
                } catch (CommitException e) {
                    Notification.show("Ошибка при обновлении профиля",
                            Type.ERROR_MESSAGE);
                }
            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public static void open(User user) {
        AppEventBus.post(new AppEvent.CloseOpenWindowsEvent());
        Window w = new ProfilePreferencesWindow(user);
        UI.getCurrent().addWindow(w);
        w.focus();
    }

    private class LogoSelectWindow extends Window {

        LogoSelectWindow() {
            Responsive.makeResponsive(this);
            setModal(true);
            GridLayout content = new GridLayout(5, 4);
            content.setSpacing(true);
            content.setMargin(true);
            setContent(content);

            int i = 0, j = 0;
            for (int logoNum = 0; logoNum < 17; logoNum++) {
                String path = "img/logo/logo" + logoNum + ".jpg";
                ThemeResource logo = new ThemeResource(path);
                Image pic = new Image(null, logo);
                pic.setWidth(100.0f, Unit.PIXELS);
                pic.addClickListener(new MouseEvents.ClickListener() {
                    @Override
                    public void click(MouseEvents.ClickEvent clickEvent) {
                        user.setLogo(logo);
                        setProfilePic(logo);
                        LogoSelectWindow.this.close();
                    }
                });
                content.addComponent(pic, i, j);
                if (i == 4) {
                    i = 0;
                    j++;
                } else {
                    i++;
                }
            }
        }
    }
}
