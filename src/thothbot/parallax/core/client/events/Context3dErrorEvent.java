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

package thothbot.parallax.core.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class Context3dErrorEvent extends GwtEvent<Context3dErrorHandler>
{

	public static Type<Context3dErrorHandler> TYPE = new Type<Context3dErrorHandler>();

	private String message;
	
    public Context3dErrorEvent(String message) 
    {
    	this.message = message;
    }
        
    public String getMessage() {
    	return this.message;
    }

    @Override
    public Type<Context3dErrorHandler> getAssociatedType() 
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Context3dErrorHandler handler) 
    {
    	handler.onContextError(this);
    }
}
