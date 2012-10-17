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

package thothbot.parallax.core.client.debugger;

import com.google.gwt.core.client.Duration;

public final class VisualGraphMs extends VisualGraphAbstract 
{

	private double ms = 0, msMin = Double.POSITIVE_INFINITY, msMax = 0;
	private double startTime = Duration.currentTimeMillis();
	
	@Override
	protected String getType() { return "ms"; }

	@Override
	protected void update(double time) 
	{
		ms = time - startTime;
		msMin = Math.min( msMin, ms );
		msMax = Math.max( msMax, ms );

		text.setInnerText( ms + " MS");
		textMin.setInnerText(msMin + "");
		textMax.setInnerText(msMax + "");
		updateGraph( graph, Math.min( 30, 30 - ( ms / 200 ) * 30 ) );
		
		startTime = time;
	}

}
