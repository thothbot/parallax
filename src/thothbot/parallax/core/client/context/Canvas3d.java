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

package thothbot.parallax.core.client.context;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.shared.Log;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusWidget;

/**
 * A canvas with WebGL support.
 */
public class Canvas3d extends FocusWidget 
{
	// "webkit-3d", "opera-3d", "moz-glweb20", "moz-webgl", "3d" 
	private static final String[] CONTEXT_IDS = { "webgl", "experimental-webgl" };

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
		canvas = Document.get().createCanvasElement();
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

			if(gl != null) break;
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
