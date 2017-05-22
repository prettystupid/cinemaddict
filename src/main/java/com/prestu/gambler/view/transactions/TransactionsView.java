package com.prestu.gambler.view.transactions;

import com.google.common.eventbus.Subscribe;
import com.prestu.gambler.GamblerAppUI;
import com.prestu.gambler.component.MovieDetailsWindow;
import com.prestu.gambler.domain.Transaction;
import com.prestu.gambler.event.AppEvent;
import com.prestu.gambler.event.AppEventBus;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.maddon.FilterableListContainer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public final class TransactionsView extends VerticalLayout implements View {

    private final Table table;
    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "dd.MM.yyyy HH:mm");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city",
            "theater", "room", "title", "seats" };

    public TransactionsView() {
        setSizeFull();
        addStyleName("transactions");
        AppEventBus.register(this);

        addComponent(buildToolbar());

        table = buildTable();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    @Override
    public void detach() {
        super.detach();
        AppEventBus.unregister(this);
    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Сеансы");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        HorizontalLayout tools = new HorizontalLayout(buildFilter());
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(final TextChangeEvent event) {
                Filterable data = (Filterable) table.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId,
                            final Item item) {

                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }

                        return filterByProperty("country", item,
                                event.getText())
                                || filterByProperty("city", item,
                                        event.getText())
                                || filterByProperty("title", item,
                                        event.getText());

                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("country")
                                || propertyId.equals("city")
                                || propertyId.equals("title")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        filter.setInputPrompt("Filter");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(new ShortcutListener("Clear",
                KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                filter.setValue("");
                ((Filterable) table.getContainerDataSource())
                        .removeAllContainerFilters();
            }
        });
        return filter;
    }

    private Table buildTable() {
        final Table table = new Table() {
            @Override
            protected String formatPropertyValue(final Object rowId,
                    final Object colId, final Property<?> property) {
                String result = super.formatPropertyValue(rowId, colId,
                        property);
                if (colId.equals("time")) {
                    result = DATEFORMAT.format(((Date) property.getValue()));
                } else if (colId.equals("price")) {
                    if (property != null && property.getValue() != null) {
                        return "$" + DECIMALFORMAT.format(property.getValue());
                    } else {
                        return "";
                    }
                }
                return result;
            }
        };
        table.setSizeFull();
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);

        table.setColumnCollapsingAllowed(true);
        table.setColumnCollapsible("time", false);
        table.setColumnCollapsible("movie", false);
        table.setColumnCollapsible("price", false);

        table.setColumnReorderingAllowed(true);
        table.setContainerDataSource(new TempTransactionsContainer(GamblerAppUI
                .getDataProvider().getRecentTransactions(200)));
        table.setSortContainerPropertyId("time");
        table.setSortAscending(false);

        table.setColumnAlignment("seats", Align.RIGHT);
        table.setColumnAlignment("price", Align.RIGHT);

        table.setVisibleColumns("time", "country", "city", "theater", "room",
                "title", "seats", "price");
        table.setColumnHeaders("Время", "Страна", "Город", "Кинотеатр", "Зал",
                "Фильм", "Мест", "Цена");

        table.setDragMode(TableDragMode.MULTIROW);
        table.setMultiSelect(true);

        table.addActionHandler(new TransactionsActionHandler());

        table.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                if (table.getValue() instanceof Set) {
                    Set<Object> val = (Set<Object>) table.getValue();
                }
            }
        });
        table.setImmediate(true);

        return table;
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (String propertyId : DEFAULT_COLLAPSIBLE) {
            if (table.isColumnCollapsed(propertyId) == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @Subscribe
    public void browserResized(final AppEvent.BrowserResizeEvent event) {
        if (defaultColumnsVisible()) {
            for (String propertyId : DEFAULT_COLLAPSIBLE) {
                table.setColumnCollapsed(propertyId, Page.getCurrent()
                        .getBrowserWindowWidth() < 800);
            }
        }
    }

    private boolean filterByProperty(final String prop, final Item item,
            final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class TransactionsActionHandler implements Handler {
        private final Action discard = new Action("Заказать");

        private final Action details = new Action("О фильме");

        @Override
        public void handleAction(final Action action, final Object sender,
                                 final Object target) {
            if (action == discard) {
                Notification.show("В процессе");
            } else if (action == details) {
                Item item = ((Table) sender).getItem(target);
                if (item != null) {
                    Long movieId = (Long) item.getItemProperty("movieId")
                            .getValue();
                    MovieDetailsWindow.open(GamblerAppUI.getDataProvider()
                            .getMovie(movieId), null, null);
                }
            }
        }

        @Override
        public Action[] getActions(final Object target, final Object sender) {
            return new Action[] { details, discard };
        }
    }

    private class TempTransactionsContainer extends
            FilterableListContainer<Transaction> {

        public TempTransactionsContainer(
                final Collection<Transaction> collection) {
            super(collection);
        }

        @Override
        public void sort(final Object[] propertyId, final boolean[] ascending) {
            final boolean sortAscending = ascending[0];
            final Object sortContainerPropertyId = propertyId[0];
            Collections.sort(getBackingList(), new Comparator<Transaction>() {
                @Override
                public int compare(final Transaction o1, final Transaction o2) {
                    int result = 0;
                    if ("time".equals(sortContainerPropertyId)) {
                        result = o1.getTime().compareTo(o2.getTime());
                    } else if ("country".equals(sortContainerPropertyId)) {
                        result = o1.getCountry().compareTo(o2.getCountry());
                    } else if ("city".equals(sortContainerPropertyId)) {
                        result = o1.getCity().compareTo(o2.getCity());
                    } else if ("theater".equals(sortContainerPropertyId)) {
                        result = o1.getTheater().compareTo(o2.getTheater());
                    } else if ("room".equals(sortContainerPropertyId)) {
                        result = o1.getRoom().compareTo(o2.getRoom());
                    } else if ("title".equals(sortContainerPropertyId)) {
                        result = o1.getTitle().compareTo(o2.getTitle());
                    } else if ("seats".equals(sortContainerPropertyId)) {
                        result = new Integer(o1.getSeats()).compareTo(o2
                                .getSeats());
                    } else if ("price".equals(sortContainerPropertyId)) {
                        result = new Double(o1.getPrice()).compareTo(o2
                                .getPrice());
                    }

                    if (!sortAscending) {
                        result *= -1;
                    }
                    return result;
                }
            });
        }

    }

}
