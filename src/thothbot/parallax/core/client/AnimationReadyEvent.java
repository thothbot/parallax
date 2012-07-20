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
 * {@link AnimatedScene} is ready for the rendering.
 * 
 * @author thothbot
 *
 */
public class AnimationReadyEvent extends GwtEvent<AnimationReadyHandler>
{
	public static Type<AnimationReadyHandler> TYPE = new Type<AnimationReadyHandler>();

    public AnimationReadyEvent() 
    {
    }

    @Override
    public Type<AnimationReadyHandler> getAssociatedType() 
    {
        return TYPE;
    }

    @Override
    protected void dispatch(AnimationReadyHandler handler) 
    {
        handler.onAnimationReady(this);
    }
}
