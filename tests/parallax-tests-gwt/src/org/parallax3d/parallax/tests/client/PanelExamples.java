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
import org.parallax3d.parallax.tests.TestAnimation;

import com.google.gwt.dom.client.Style.Unit;
import org.parallax3d.parallax.tests.TestList;
import org.parallax3d.parallax.tests.client.widgets.CategoryLarge;

import java.util.List;
import java.util.Map;

/**
 * This widget used to show all available examples
 */
public class PanelExamples extends ScrollPanel
{
	
	/**
	 * Used to show content of the all tree categories
	 */
	VerticalPanel categoriesInfo;

	public PanelExamples()
	{
		categoriesInfo = new VerticalPanel();
		categoriesInfo.getElement().getStyle().setMarginLeft(20.0, Unit.PX);
		categoriesInfo.getElement().getStyle().setMarginRight(20.0, Unit.PX);
		this.add(categoriesInfo);

		for(Map.Entry<String, List<? extends TestAnimation>> entry: TestList.DATA.entrySet()) {
			this.categoriesInfo.add(new CategoryLarge(entry.getKey(), entry.getValue()));
		}
	}
}
