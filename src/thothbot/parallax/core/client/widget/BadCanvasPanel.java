/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client.widget;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget is used when the user's browser can not initialize 
 * a canvas in 3D context.
 * 
 * @author thothbot
 *
 */
public class BadCanvasPanel extends InfoPanel
{
	private String msg;

	public BadCanvasPanel(String msg)
	{
		this.msg = msg;
	}
	@Override
	public Widget getContent()
	{
		return new Label("Can not initialize Canvas: " + this.msg);
	}	
}
