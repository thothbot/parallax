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

package org.parallax3d.parallax.backends.gwt.client.events;

import org.parallax3d.parallax.backends.gwt.client.AnimatedScene;
import org.parallax3d.parallax.graphics.renderers.WebGLRenderer;
import org.parallax3d.parallax.backends.gwt.client.RenderingPanel;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The event is called when the {@link RenderingPanel} is fully initialized and 
 * {@link AnimatedScene} is ready for the {@link WebGLRenderer}.
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
