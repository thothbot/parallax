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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import org.parallax3d.parallax.tests.TestAnimation;
import org.parallax3d.parallax.tests.TestList;

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

	@UiField(provided = true)
	LayoutDock docked;

	/**
	 * Main panel where will be RenderingPanel located
	 */
	@UiField(provided = true)
	SimpleLayoutPanel content;
	
	public LayoutMain()
	{
		content = new SimpleLayoutPanel();

		docked = new LayoutDock();

		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Default to no content.
		content.ensureDebugId("content");
		menu.ensureDebugId("menu");

		for(Map.Entry<String, List<? extends TestAnimation>> entry: TestList.DATA.entrySet()) {
			addCategory(entry);
		}

	}

	private void addCategory(Map.Entry<String, List<? extends TestAnimation>> entry)
	{
		Label name = new Label(entry.getKey());
		name.setStyleName("indexGroupName");

		this.menu.add(name);
		FlowPanel examplesInfo = new FlowPanel();
		examplesInfo.ensureDebugId("examplesInfo");

		this.menu.add(examplesInfo);

		for (TestAnimation example : entry.getValue())
			addItem(example, examplesInfo);
	}

	/**
	 * Used to generate view of the examples. On click will be updated
	 * selection model.
	 *
	 * @param example content Widget
	 * @param examplesInfo panel where render this view
	 */
	private void addItem(final TestAnimation example, FlowPanel examplesInfo)
	{

		final FlowPanel examplePanel = new FlowPanel();
		examplePanel.setStyleName("indexExamplePanel");
		examplePanel.ensureDebugId("examplePanel");

		final SimplePanel img = new SimplePanel();
		img.setStyleName("indexExampleImage");
		examplePanel.add(new Image(example.getIconUrl()));

		Label name = new Label(example.getName());
		name.setStyleName("text");
		examplePanel.add(name);

		Label description = new Label(example.getDescription());
		description.setStyleName("description");
		examplePanel.add(description);

		examplesInfo.add(examplePanel);
		examplePanel.sinkEvents(Event.ONCLICK);

		examplePanel.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				History.newItem("!"+example.getContentWidgetToken(), true);
			}
		}, ClickEvent.getType());
	}

	public LayoutDock getDock() {
		return docked;
	}

	public void setContentWidget(SimplePanel content)
	{
		this.content.setWidget(content);
	}

	public void setContentWidget(PanelExample content)
	{
		this.content.setWidget(content);
	}	
}
