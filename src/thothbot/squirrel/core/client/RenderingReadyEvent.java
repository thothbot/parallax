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

package thothbot.squirrel.core.client;

import com.google.gwt.event.shared.GwtEvent;

public class RenderingReadyEvent extends GwtEvent<RenderingReadyHandler>
{
	public static Type<RenderingReadyHandler> TYPE = new Type<RenderingReadyHandler>();

    public RenderingReadyEvent() 
    {
    }

    @Override
    public Type<RenderingReadyHandler> getAssociatedType() 
    {
        return TYPE;
    }

    @Override
    protected void dispatch(RenderingReadyHandler handler) 
    {
        handler.onAnimationReady(this);
    }
}
