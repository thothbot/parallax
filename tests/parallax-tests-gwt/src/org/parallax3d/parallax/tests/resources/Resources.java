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
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * The resources and styles used throughout the Demo.
 */
public interface Resources extends ClientBundle
{
	@Source("logo.svg")
	@DataResource.MimeType("image/svg+xml")
	DataResource logo();

	@Source("common.css")
	@CssResource.NotStrict
	CssResource css();

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
}
