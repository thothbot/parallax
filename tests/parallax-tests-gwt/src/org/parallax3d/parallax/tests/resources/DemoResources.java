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

package org.parallax3d.parallax.tests.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * The resources and styles used throughout the Demo.
 */
public interface DemoResources extends ClientBundle
{
	/**
	 * The text color of the selected link.
	 */
	String SELECTED_TAB_COLOR = "#EEEEEE";
	
	/**
	 * The path to source code for examples.
	 */
	String DST_SOURCE = "demoSource/";
	
	String DST_FACEBOOK = "fb/";

	/**
	 * The destination folder for parsed source code from examples.
	 */
	String DST_SOURCE_EXAMPLE = DST_SOURCE + "java/";
	
	@Source("images/logo.png")
	ImageResource logo();
	
	@Source("images/loading.gif")
	ImageResource loading();
	
	@Source("images/s_fullscreen.png")
	ImageResource switchFullscreen();
	
	@Source("images/s_animation.png")
	ImageResource switchAnimation();
	
	@Source("images/s_oculusrift.png")
	ImageResource switchEOculusRift();
	
	@Source("images/s_anaglyph.png")
	ImageResource switchEAnaglyph();
	
	@Source("images/s_parallaxbarrier.png")
	ImageResource switchEParallaxBarrier();
	
	@Source("images/s_stereo.png")
	ImageResource switchEStereo();
	
	@Source("images/s_noeffects.png")
	ImageResource switchEffectNone();
	
	@Source("css/demoView.css")
	@CssResource.NotStrict
	CssResource css();	
}
