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

package org.parallax3d.parallax.tests.client;

import org.parallax3d.parallax.tests.resources.DemoResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Main view of the application
 */
public class PanelTop extends ResizeComposite
{
	private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

	interface PanelUiBinder extends UiBinder<Widget, PanelTop> {
	}

	/**
	 * The button used to show index widget.
	 */
	@UiField
	Anchor linkIndex;
	
	@UiField(provided=true)
	HorizontalPanel bottonPanel;
	
	/**
	 * The page content
	 */
	@UiField(provided=true)
	SimpleLayoutPanel contentWidget;

	public PanelTop()
	{
		bottonPanel = new HorizontalPanel();
		contentWidget = new SimpleLayoutPanel();
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Anchor getTabIndex()
	{
		return this.linkIndex;
	}
	
	public void setContentWidget(SimplePanel content)
	{
		linkIndex.getElement().getStyle().setColor(DemoResources.SELECTED_TAB_COLOR);
		bottonPanel.setVisible(false);
		
		this.contentWidget.setWidget(content);
	}

	public void setContentWidget(PanelMain content)
	{
		linkIndex.getElement().getStyle().clearColor();
		bottonPanel.setVisible(true);

		this.contentWidget.setWidget(content);	
	}	
}
