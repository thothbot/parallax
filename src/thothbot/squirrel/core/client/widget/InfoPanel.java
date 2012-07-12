/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.client.widget;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class InfoPanel extends LayoutPanel
{
	AbsolutePanel popupPanel;

	public abstract Widget getContent();

	public InfoPanel()
	{   
		this.popupPanel = new AbsolutePanel();

		this.popupPanel.setStyleName("common-panel", true);
		
		this.add(this.popupPanel);
		
		this.popupPanel.add(getContent());
	}

	public void show()
	{
		this.popupPanel.setVisible(true);
	}
	
	public void hide()
	{
		this.popupPanel.setVisible(false);
	}
}
