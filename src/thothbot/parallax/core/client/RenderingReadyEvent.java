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

package thothbot.parallax.core.client;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The event is called when the {@link RenderingPanel} is fully initialized and 
 * {@link RenderingScene} is ready for the rendering.
 * 
 * @author thothbot
 *
 */
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
