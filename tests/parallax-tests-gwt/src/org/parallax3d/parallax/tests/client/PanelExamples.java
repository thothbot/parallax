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

import org.parallax3d.parallax.tests.TestAnimation;
import org.parallax3d.parallax.tests.client.DataModel.Category;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This widget used to show all available examples
 */
public class PanelExamples extends ScrollPanel
{
	
	/**
	 * Used to show content of the all tree categories
	 */
	VerticalPanel categoriesInfo;
	
	/**
	 * Three model, which data used to generate the page
	 */
	DataModel treeModel;
		
	public PanelExamples(DataModel treeModel)
	{
		this.treeModel = treeModel;
		
		categoriesInfo = new VerticalPanel();
		categoriesInfo.getElement().getStyle().setMarginLeft(10.0, Unit.PX);
		categoriesInfo.getElement().getStyle().setMarginRight(10.0, Unit.PX);
		this.add(categoriesInfo);

		for (Category category : treeModel.getCategories()) 
		{
			addCategory(category);
		}
	}
	
	/**
	 * Generate the view of the top level Category
	 * 
	 * @param category top-level category
	 */
	public void addCategory(Category category)
	{
		Label name = new Label(category.getName());
		name.setStyleName("indexGroupName");
		
		this.categoriesInfo.add(name);
		FlowPanel examplesInfo = new FlowPanel();
		examplesInfo.ensureDebugId("examplesInfo");

		this.categoriesInfo.add(examplesInfo);

		for (TestAnimation example : category.getExamples().getList())
			addItem(example, examplesInfo);
	}
	
	/**
	 * Used to generate view of the examples. On click will be updated
	 * selection model.
	 * 
	 * @param example content Widget
	 * @param examplesInfo panel where render this view 
	 */
	public void addItem(final TestAnimation example, FlowPanel examplesInfo)
	{

		final FlowPanel examplePanel = new FlowPanel();
		examplePanel.setStyleName("indexExamplePanel");
		examplePanel.ensureDebugId("examplePanel");
		examplePanel.add(new Image(example.getIconUrl()));
		
		Label name = new Label(example.getName());
		name.setStyleName("text");
		examplePanel.add(name);

		examplesInfo.add(examplePanel);
		examplePanel.sinkEvents(Event.ONCLICK);
		
		examplePanel.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event)
			{
				treeModel.getSelectionModel().setSelected(example, true);
				
			}
		}, ClickEvent.getType());
	}
}
