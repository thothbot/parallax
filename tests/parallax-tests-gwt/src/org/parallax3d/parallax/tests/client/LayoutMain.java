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

import com.google.gwt.user.client.ui.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import org.parallax3d.parallax.tests.TestAnimation;
import org.parallax3d.parallax.tests.TestList;
import org.parallax3d.parallax.tests.client.widgets.CategorySmall;
import org.parallax3d.parallax.tests.client.widgets.ItemInfo;
import org.parallax3d.parallax.tests.client.widgets.Logo;

import java.util.List;
import java.util.Map;

/**
 * Main view of the application
 */
public class LayoutMain extends ResizeComposite
{
	private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

	interface PanelUiBinder extends UiBinder<Widget, LayoutMain> {
	}

	/**
	 * The main menu used to navigate to examples.
	 */
	@UiField
	VerticalPanel menu;

	@UiField
	Logo logo;

	/**
	 * Main panel where will be RenderingPanel located
	 */
	@UiField
	SimplePanel content;

	PanelExample panelExample;
	
	public LayoutMain()
	{
		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Default to no content.
		content.ensureDebugId("content");
		menu.ensureDebugId("menu");

		for(Map.Entry<String, List<? extends TestAnimation>> entry: TestList.DATA.entrySet()) {
			this.menu.add(new CategorySmall(entry.getKey(), entry.getValue()));
		}
	}

	@Override
	protected void onLoad() {

		if(panelExample == null)
		{
			panelExample = new PanelExample();
			content.add(panelExample);
		}

		super.onLoad();
	}

	public void setAnimation(final TestAnimation animation)
	{
		logo.setInfoPanel(new ItemInfo(animation));
		panelExample.setAnimation(animation);
	}


}
