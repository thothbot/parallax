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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import org.parallax3d.parallax.tests.client.WebApp;

public class Logo extends Composite {

    private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

    interface PanelUiBinder extends UiBinder<Widget, Logo> {
    }

    @UiField
    Image logo;

    @UiField
    Anchor linkIndex;

    @UiField
    FlowPanel info;

    public Logo()
    {
        // Initialize the ui binder.
        initWidget(uiBinder.createAndBindUi(this));

        logo.setUrl(WebApp.resources.logo().getSafeUri());

        linkIndex.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                History.newItem("", true);
            }
        });
    }

    public void setInfoPanel(ItemInfo info)
    {
        this.info.clear();
        this.info.add(info);
    }

}
