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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import org.parallax3d.parallax.tests.TestAnimation;
import org.parallax3d.parallax.tests.TestList;
import org.parallax3d.parallax.tests.geometries.GeometryCube;

/**
 * The {@link TreeViewModel} used by the main menu.
 */
public class DataModel implements TreeViewModel 
{
	/**
	 * The cell used to render categories.
	 */
	private static class CategoryCell extends AbstractCell<Category> 
	{
		@Override
		public void render(Context context, Category value, SafeHtmlBuilder sb) 
		{
			if (value != null)
				sb.appendEscaped(value.getName());
		}
	}

	/**
	 * The cell used to render examples.
	 */
	private static class ContentWidgetCell extends AbstractCell<TestAnimation>
	{    
		@Override
		public void render(Context context, TestAnimation value, SafeHtmlBuilder sb)
		{
			if (value != null) 
			{
				sb.appendHtmlConstant("<img class='menuIcon' src='" + value.getIconUrl() + "'/>");
				sb.appendHtmlConstant("<span>" + value.getName() + "</span>");
			}
		}
	}

	/**
	 * A top level category in the tree.
	 */
	public class Category 
	{
		private final ListDataProvider<TestAnimation> examples = new ListDataProvider<TestAnimation>();
		private final String name;
		private NodeInfo<TestAnimation> nodeInfo;

		public Category(Map.Entry<String, List<? extends TestAnimation>> entry) {
			this.name = entry.getKey();

			List<? extends TestAnimation> values = entry.getValue();
			examples.getList().addAll( values );

			for(TestAnimation value : values) {
				contentToken.put(value.getContentWidgetToken(), value);
			}
		}

		public ListDataProvider<TestAnimation> getExamples()
		{
			return this.examples;
		}

		public String getName() 
		{
			return name;
		}

		/**
		 * Get the node info for examples under this category.
		 * 
		 * @return the node info
		 */
		public NodeInfo<TestAnimation> getNodeInfo()
		{
			if (nodeInfo == null)
				nodeInfo = new DefaultNodeInfo<TestAnimation>(getExamples(), contentWidgetCell, selectionModel, null);
			
			return nodeInfo;
		}
	}

	/**
	 * The top level categories.
	 */
	private final ListDataProvider<Category> categories = new ListDataProvider<Category>();

	/**
	 * A mapping of {@link PanelExample}s to their associated categories.
	 */
	private final Map<TestAnimation, Category> contentCategory = new HashMap<TestAnimation, Category>();

	/**
	 * The cell used to render examples.
	 */
	private final ContentWidgetCell contentWidgetCell = new ContentWidgetCell();

	/**
	 * A mapping of history tokens to their associated {@link PanelExample}.
	 */
	private final Map<String, TestAnimation> contentToken = new HashMap<String, TestAnimation>();

	/**
	 * The selection model used to select examples.
	 */
	private final SelectionModel<TestAnimation> selectionModel;

	public DataModel(SelectionModel<TestAnimation> selectionModel)
	{
		this.selectionModel = selectionModel;

		List<Category> categoriesList = categories.getList();
		for(Map.Entry<String, List<? extends TestAnimation>> entry: TestList.DATA.entrySet()) {
			categoriesList.add(new Category(entry));
		}
	}

	public  SelectionModel<TestAnimation> getSelectionModel()
	{
		return this.selectionModel;
	}

	/**
	 * Get the {@link Category} associated with a widget.
	 * 
	 * @param widget the {@link PanelExample}
	 * @return the associated {@link Category}
	 */
	public Category getCategoryForContentWidget(TestAnimation widget)
	{
		return contentCategory.get(widget);
	}

	/**
	 * Get the content widget associated with the specified history token.
	 * 
	 * @param token the history token
	 * @return the associated {@link PanelExample}
	 */
	public TestAnimation getContentWidgetForToken(String token)
	{
		return contentToken.get(token);
	}

	public <T> NodeInfo<?> getNodeInfo(T value) 
	{
		if (value == null) 
		{
			// Return the top level categories.
			return new DefaultNodeInfo<Category>(categories, new CategoryCell());
		} 
		else if (value instanceof Category) 
		{
			// Return the examples within the category.
			Category category = (Category) value;
			return category.getNodeInfo();
		}
		return null;
	}

	public boolean isLeaf(Object value) 
	{
		return value != null && !(value instanceof Category);
	}

	public List<Category> getCategories()
	{
		return categories.getList();
	}
	/**
	 * Get the set of all {@link PanelExample}s used in the model.
	 * 
	 * @return the {@link PanelExample}s
	 */
	public Set<TestAnimation> getAllContentWidgets()
	{
		Set<TestAnimation> widgets = new HashSet<TestAnimation>();
		for (Category category : getCategories()) 
		{
			for (TestAnimation example : category.examples.getList())
				widgets.add(example);
		}

		return widgets;
	}
}
