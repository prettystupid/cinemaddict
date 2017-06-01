package com.prestu.gambler.component;

import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;

public abstract class DifficultSelector extends HorizontalLayout {

    public DifficultSelector() {
        super();
        ComboBox listSelect = new ComboBox();

        listSelect.addItems("Easy", "Medium", "Hard");

        listSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String difficult = (String) valueChangeEvent.getProperty().getValue();

                switch (difficult) {
                    case "Easy":
                        easy();
                        break;
                    case "Medium":
                        medium();
                        break;
                    case "Hard":
                        hard();
                        break;
                }
            }
        });

        listSelect.setNullSelectionAllowed(false);
        listSelect.setValue("Easy");

        setSpacing(false);
        setMargin(false);
        addComponent(listSelect);
    }

    protected abstract void easy();
    protected abstract void medium();
    protected abstract void hard();
}
