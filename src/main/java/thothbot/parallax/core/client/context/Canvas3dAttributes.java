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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Canvas3dAttributes interface contains drawing surface attributes and is 
 * passed as the second parameter to getContext.
 * 
 * @author hao1300@gmail.com
 */
public class Canvas3dAttributes {
	private final Canvas3dAttributesImpl impl;
	
	public Canvas3dAttributes() {
		impl = JavaScriptObject.createObject().cast();
	}
	
	/**
	 * Converts a JavaScriptObject to this Canvas3dAttributes.
	 *  
	 * @param contextAttribs a JavaScriptObject whose type is 
	 * 				Canvas3dAttributes.
	 */
	Canvas3dAttributes(JavaScriptObject contextAttribs) {
		impl = contextAttribs.cast();
	}
	
	/**
	 * Gets the JavaScriptObject for the 
	 */
	public JavaScriptObject getGLContextAttributesImpl() {
		return impl;		
	}
	
	/**
	 * Default: true. If the value is true, the drawing buffer has an alpha 
	 * channel for the purposes of performing OpenGL destination alpha operations 
	 * and compositing with the page. If the value is false, no alpha buffer is 
	 * available.
	 * 
	 * @param enable
	 */
	public void setAlphaEnable(boolean enable) {
		impl.setAlphaEnable(enable);
	}
	
	/**
	 * Default: true. If the value is true, the drawing buffer has a depth buffer 
	 * of at least 16 bits. If the value is false, no depth buffer is available.
	 * 
	 * @param enable
	 */
	public void setDepthEnable(boolean enable) {
		impl.setDepthEnable(enable);
	}
	
	/**
	 * Default: false. If the value is true, the drawing buffer has a stencil 
	 * buffer of at least 8 bits. If the value is false, no stencil buffer is 
	 * available.
	 * 
	 * @param enable
	 */
	public void setStencilEnable(boolean enable) {
		impl.setStencilEnable(enable);
	}
	
	/**
	 * Default: true. If the value is true and the implementation supports 
	 * antialiasing the drawing buffer will perform antialiasing using its 
	 * choice of technique (multisample/supersample) and quality. If the value 
	 * is false or the implementation does not support antialiasing, no 
	 * antialiasing is performed.
	 * 
	 * @param enable
	 */
	public void setAntialiasEnable(boolean enable) {
		impl.setAntialiasEnable(enable);
	}
	
	/**
	 * Default: true. If the value is true the page compositor will assume the 
	 * drawing buffer contains colors with premultiplied alpha. If the value is 
	 * false the page compositor will assume that colors in the drawing buffer 
	 * are not premultiplied. This flag is ignored if the alpha flag is false.
	 * 
	 * @param enable
	 */
	public void setPremultipliedAlphaEnable(boolean enable) {
		impl.setPremultipliedAlphaEnable(enable);
	}
	
	/**
	 * Default: false. If false, once the drawing buffer is presented as described 
	 * in the Drawing Buffer section, the contents of the drawing buffer are 
	 * cleared to their default values. All elements of the drawing buffer (color, 
	 * depth and stencil) are cleared. If the value is true the buffers will not 
	 * be cleared and will preserve their values until cleared or overwritten by 
	 * the author.
	 * 
	 * @param enable
	 */
	public void setPreserveDrawingBufferEnable(boolean enable) {
		impl.setPreserveDrawingBufferEnable(enable);
	}

	/**
	 * @see #setAlphaEnable(boolean)
	 */
	public boolean isAlphaEnable() {
		return impl.isAlphaEnable();
	}
	
	/**
	 * @see #setDepthEnable(boolean)
	 */
	public boolean isDepthEnable() {
		return impl.isDepthEnable();
	}
	
	/**
	 * @see #setStencilEnable(boolean)
	 */
	public boolean isStencilEnable() {
		return impl.isStencilEnable();
	}
	
	/**
	 * @see #setAntialiasEnable(boolean)
	 */
	public boolean isAntialiasEnable() {
		return impl.isAntialiasEnable();
	}
	
	/**
	 * @see #setPremultipliedAlphaEnable(boolean)
	 */
	public boolean isPremultipliedAlphaEnable() {
		return impl.isPremultipliedAlphaEnable();
	}
	
	/**
	 * @see #setPreserveDrawingBufferEnable(boolean)
	 */
	public boolean isPreserveDrawingBufferEnable() {
		return impl.isPreserveDrawingBufferEnable();
	}
	
	/**
	 * JavaScriptObject that wraps the functionalities of Canvas3dAttributes.
	 * 
	 * @author hao1300@gmail.com
	 */
	private static class Canvas3dAttributesImpl extends JavaScriptObject {
		protected Canvas3dAttributesImpl() {
		}
		
		public native final void setAlphaEnable(boolean enable) /*-{
			this.alpha = enable;
		}-*/;
		
		public native final void setDepthEnable(boolean enable) /*-{
			this.depth = enable;
		}-*/;
		
		public native final void setStencilEnable(boolean enable) /*-{
			this.stencil = enable;
		}-*/;
		
		public native final void setAntialiasEnable(boolean enable) /*-{
			this.antialias = enable;
		}-*/;
		
		public native final void setPremultipliedAlphaEnable(boolean enable) /*-{
			this.premultipliedAlpha = enable;
		}-*/;
		
		public native final void setPreserveDrawingBufferEnable(boolean enable) /*-{
			this.preserveDrawingBuffer = enable;
		}-*/;

		public native final boolean isAlphaEnable() /*-{
			return this.alpha;
		}-*/;
		
		public native final boolean isDepthEnable() /*-{
			return this.depth;
		}-*/;
		
		public native final boolean isStencilEnable() /*-{
			return this.stencil;
		}-*/;
		
		public native final boolean isAntialiasEnable() /*-{
			return this.antialias;
		}-*/;
		
		public native final boolean isPremultipliedAlphaEnable() /*-{
			return this.premultipliedAlpha;
		}-*/;
		
		public native final boolean isPreserveDrawingBufferEnable() /*-{
			return this.preserveDrawingBuffer;
		}-*/;
	}
}
