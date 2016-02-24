/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 *
 * This file is part of Parallax project.
 *
 * Parallax is free software: you can redistribute it and/or modify it
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 *
 * Parallax is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution
 * 3.0 Unported License. for more details.
 *
 * You should have received a copy of the the Creative Commons Attribution
 * 3.0 Unported License along with Parallax.
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.parallax.tests.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.parallax3d.parallax.tests.ParallaxTest;

public class ItemSmall extends Composite {

    private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

    interface PanelUiBinder extends UiBinder<Widget, ItemSmall> {
    }

    @UiField
    Image image;

    @UiField
    Label name;

    @UiField
    Label category;

    public ItemSmall(String category, final ParallaxTest animation)
    {
        // Initialize the ui binder.
        initWidget(uiBinder.createAndBindUi(this));

        this.name.setText(animation.getName());
        this.category.setText(category);

        this.image.setUrl(animation.getIconUrl());

        this.sinkEvents(Event.ONCLICK);
        this.addHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event)
            {
                History.newItem("!"+animation.getTestName(), true);
            }
        }, ClickEvent.getType());
    }
}
