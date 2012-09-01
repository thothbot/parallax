/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the code written by Hao Nguyen.
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

package thothbot.parallax.core.client.context;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.shared.Log;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusWidget;

/**
 * A canvas with WebGL support.
 */
public class Canvas3d extends FocusWidget 
{
	private static final String[] CONTEXT_IDS = { "experimental-webgl", "webgl", "webkit-3d", "opera-3d", "moz-glweb20", "moz-webgl", "3d" };

	private WebGLRenderingContext gl;	
	private final CanvasElement canvas;
	
	/**
	 * Initialize a canvas3d of the given size. 
	 * @param attribs GL 
	 * 			Canvas attributes, represented in {@link Canvas3dAttributes}.
	 * 
	 * @throws Canvas3dException 
	 */
	public Canvas3d(Canvas3dAttributes attribs) throws Exception 
	{
		canvas = Document.get().createElement("canvas").cast();
		setElement(canvas);

		for (final String contextId : CONTEXT_IDS) 
		{
			try
			{
				Log.debug("Trying 3d context: " + contextId);
				if (attribs == null)
					gl = (WebGLRenderingContext) canvas.getContext(contextId);
				else
					gl = getContext(canvas, contextId, attribs.getGLContextAttributesImpl());
			}
			catch(Exception e)
			{
				gl = null;
				Log.error("3d context: " + contextId + ", " + e.getMessage());
			}
		}
		
		if( gl == null )
			throw new Canvas3dException("GL context can not be initialized. ");
	}
	
	private static native WebGLRenderingContext getContext(CanvasElement canvas, String contextId,
			JavaScriptObject glContextAttr) /*-{
		return canvas.getContext(contextId, glContextAttr);
    }-*/;
	
	/**
	 * Gets the width of the canvas3d.
	 */
	public int getWidth() 
	{
		return canvas.getWidth();
	}
	
	/**
	 * Gets the height of the canvas3d.
	 */
	public int getHeight() 
	{
		return canvas.getHeight();
	}
	
	public double getAspectRation()
	{
		return (double)getWidth() / getHeight();
	}
	
	/**
	 * Resizes the canvas3d.
	 * 
	 * @param width the new width of the canvas3d.
	 * @param height the new height of the canvas3d.
	 */
	public void setSize(int width, int height) 
	{
		setWidth(width);
		setHeight(height);
	}
	
	/**
	 * Sets the width of the canvas3d.
	 * @param width the new width of the canvas3d.
	 */
	public void setWidth(int width) 
	{
		canvas.setWidth(width);
	}
	
	/**
	 * Sets the height of the canvas3d.
	 * @param height the new height of the canvas3d.
	 */
	public void setHeight(int height) 
	{
		canvas.setHeight(height);
	}

	/**
	 * Gets the canvas element.
	 * 
	 * @return the underlying canvas element.
	 */
	public CanvasElement getCanvas() 
	{
		return canvas;
	}
	
	/**
	 * Gets the WebGL context.
	 * 
	 * @return the underlying context implementation for drawing onto the canvas,
	 * 				 or null if no context implementation is available.
	 */
	public WebGLRenderingContext getGL() 
	{
		return gl;
	}

	public HandlerRegistration addWebGLContextLostEventListener(Context3dLostHandler handler) 
	{
		return addBitlessDomHandler(handler, Context3dLostEvent.getType());
	}
}
