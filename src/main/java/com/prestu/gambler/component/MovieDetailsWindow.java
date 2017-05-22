package com.prestu.gambler.component;

import com.prestu.gambler.domain.Movie;
import com.prestu.gambler.event.AppEvent;
import com.prestu.gambler.event.AppEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class MovieDetailsWindow extends Window {

    private final Label synopsis = new Label();

    private MovieDetailsWindow(final Movie movie, final Date startTime,
                               final Date endTime) {
        addStyleName("moviedetailswindow");
        Responsive.makeResponsive(this);

        setCaption(movie.getTitle());
        center();
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);

        Panel detailsWrapper = new Panel(buildMovieDetails(movie, startTime,
                endTime));
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
        detailsWrapper.addStyleName("scroll-divider");
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        content.addComponent(buildFooter());
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Закрыть");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                close();
            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    private Component buildMovieDetails(final Movie movie,
                                        final Date startTime, final Date endTime) {
        HorizontalLayout details = new HorizontalLayout();
        details.setWidth(100.0f, Unit.PERCENTAGE);
        details.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        details.setMargin(true);
        details.setSpacing(true);

        final Image coverImage = new Image(null, new ExternalResource(
                movie.getThumbUrl()));
        coverImage.addStyleName("cover");
        details.addComponent(coverImage);

        Component detailsForm = buildDetailsForm(movie, startTime, endTime);
        details.addComponent(detailsForm);
        details.setExpandRatio(detailsForm, 1);

        return details;
    }

    private Component buildDetailsForm(final Movie movie, final Date startTime,
                                       final Date endTime) {
        FormLayout fields = new FormLayout();
        fields.setSpacing(false);
        fields.setMargin(false);

        Label label;
        SimpleDateFormat df = new SimpleDateFormat();
        if (startTime != null) {
            df.applyPattern("dd.MM.yyyy");
            label = new Label(df.format(startTime));
            label.setSizeUndefined();
            label.setCaption("Дата");
            fields.addComponent(label);

            df.applyPattern("HH:mm");
            label = new Label(df.format(startTime));
            label.setSizeUndefined();
            label.setCaption("Начало");
            fields.addComponent(label);
        }

        if (endTime != null) {
            label = new Label(df.format(endTime));
            label.setSizeUndefined();
            label.setCaption("Заканчивается");
            fields.addComponent(label);
        }

        label = new Label(movie.getDuration() + " минут");
        label.setSizeUndefined();
        label.setCaption("Длительность");
        fields.addComponent(label);

        synopsis.setData(movie.getSynopsis());
        synopsis.setCaption("Краткий обзор");
        updateSynopsis(movie, false);
        fields.addComponent(synopsis);

        final Button more = new Button("Показать полностью…");
        more.addStyleName(ValoTheme.BUTTON_LINK);
        fields.addComponent(more);
        more.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                updateSynopsis(null, true);
                event.getButton().setVisible(false);
                MovieDetailsWindow.this.focus();
            }
        });

        return fields;
    }

    private void updateSynopsis(final Movie m, final boolean expand) {
        String synopsisText = synopsis.getData().toString();
        if (m != null) {
            synopsisText = m.getSynopsis();
            synopsis.setData(m.getSynopsis());
        }
        if (!expand) {
            synopsisText = synopsisText.length() > 300 ? synopsisText
                    .substring(0, 300) + "…" : synopsisText;

        }
        synopsis.setValue(synopsisText);
    }

    public static void open(final Movie movie, final Date startTime,
            final Date endTime) {
        AppEventBus.post(new AppEvent.CloseOpenWindowsEvent());
        Window w = new MovieDetailsWindow(movie, startTime, endTime);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}
