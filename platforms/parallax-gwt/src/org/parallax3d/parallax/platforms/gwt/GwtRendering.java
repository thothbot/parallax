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

package org.parallax3d.parallax.platforms.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.webgl.client.WebGLContextAttributes;
import com.google.gwt.webgl.client.WebGLRenderingContext;
import org.parallax3d.parallax.App;
import org.parallax3d.parallax.Rendering;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.system.ParallaxRuntimeException;
import org.parallax3d.parallax.system.gl.GL20;

public class GwtRendering extends Rendering {

	CanvasElement canvas;
	WebGLRenderingContext context;
	GLRenderer renderer;
	GL20 gl;

	String extensions;
	float fps = 0;
	long lastTimeStamp = System.currentTimeMillis();
	long frameId = -1;
	float deltaTime = 0;
	float time = 0;
	int frames;

	GwtAppConfiguration config;

	public GwtRendering(Panel root, GwtAppConfiguration config) {
		Canvas canvasWidget = Canvas.createIfSupported();
		if (canvasWidget == null)
			throw new ParallaxRuntimeException("Canvas not supported");

		int width  = root.getOffsetWidth();
		int height = root.getOffsetHeight();

		canvas = canvasWidget.getCanvasElement();
		root.add(canvasWidget);
		canvas.setWidth(width);
		canvas.setHeight(height);
		this.config = config;

		WebGLContextAttributes attributes = WebGLContextAttributes.create();
		attributes.setAntialias(config.antialiasing);
		attributes.setStencil(config.stencil);
		attributes.setAlpha(config.alpha);
		attributes.setPremultipliedAlpha(config.premultipliedAlpha);
		attributes.setPreserveDrawingBuffer(config.preserveDrawingBuffer);

		context = WebGLRenderingContext.getContext(canvas, attributes);
		context.viewport(0, 0, width, height);

		App.gl = gl = new GwtGL20(context);

		renderer = new GLRenderer(width, height);
	}

	public WebGLRenderingContext getContext () {
		return context;
	}

	public GLRenderer getRenderer() {
		return renderer;
	}

	@Override
	public GL20 getGL20 () {
		return gl;
	}

	@Override
	public int getWidth () {
		return canvas.getWidth();
	}

	@Override
	public int getHeight () {
		return canvas.getHeight();
	}

	@Override
	public long getFrameId () {
		return frameId;
	}

	@Override
	public float getDeltaTime () {
		return deltaTime;
	}

	@Override
	public int getFramesPerSecond () {
		return (int)fps;
	}

	@Override
	public float getPpiX () {
		return 96;
	}

	@Override
	public float getPpiY () {
		return 96;
	}

	@Override
	public boolean supportsDisplayModeChange () {
		return supportsFullscreenJSNI();
	}

	private native boolean supportsFullscreenJSNI () /*-{
		if ("fullscreenEnabled" in $doc) {
			return $doc.fullscreenEnabled;
		}
		if ("webkitFullscreenEnabled" in $doc) {
			return $doc.webkitFullscreenEnabled;
		}
		if ("mozFullScreenEnabled" in $doc) {
			return $doc.mozFullScreenEnabled;
		}
		if ("msFullscreenEnabled" in $doc) {
			return $doc.msFullscreenEnabled;
		}
		return false;
	}-*/;

	private native int getScreenWidthJSNI () /*-{
		return $wnd.screen.width;
	}-*/;

	private native int getScreenHeightJSNI () /*-{
		return $wnd.screen.height;
	}-*/;

	private native boolean isFullscreenJSNI () /*-{
		// Standards compliant check for fullscreen
		if ("fullscreenElement" in $doc) {
			return $doc.fullscreenElement != null;
		}
		// Vendor prefixed versions of standard check
		if ("msFullscreenElement" in $doc) {
			return $doc.msFullscreenElement != null;
		}
		if ("webkitFullscreenElement" in $doc) {
			return $doc.webkitFullscreenElement != null;
		}
		if ("mozFullScreenElement" in $doc) { // Yes, with a capital 'S'
			return $doc.mozFullScreenElement != null;
		}
		// Older, non-standard ways of checking for fullscreen
		if ("webkitIsFullScreen" in $doc) {
			return $doc.webkitIsFullScreen;
		}
		if ("mozFullScreen" in $doc) {
			return $doc.mozFullScreen;
		}
		return false
	}-*/;

	private void fullscreenChanged () {
		if (!isFullscreen()) {
//			canvas.setWidth(config.width);
//			canvas.setHeight(config.height);
		}
	}

	private native boolean setFullscreenJSNI (GwtRendering graphics, CanvasElement element) /*-{
		// Attempt to use the non-prefixed standard API (https://fullscreen.spec.whatwg.org)
		if (element.requestFullscreen) {
			element.width = $wnd.screen.width;
			element.height = $wnd.screen.height;
			element.requestFullscreen();
			$doc.addEventListener(
				"fullscreenchange",
				function() {
					graphics.@org.parallax3d.parallax.platforms.gwt.GwtRendering::fullscreenChanged()();
				}, false);
			return true;
		}
		// Attempt to the vendor specific variants of the API
		if (element.webkitRequestFullScreen) {
			element.width = $wnd.screen.width;
			element.height = $wnd.screen.height;
			element.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
			$doc.addEventListener(
				"webkitfullscreenchange",
				function() {
					graphics.@org.parallax3d.parallax.platforms.gwt.GwtRendering::fullscreenChanged()();
				}, false);
			return true;
		}
		if (element.mozRequestFullScreen) {
			element.width = $wnd.screen.width;
			element.height = $wnd.screen.height;
			element.mozRequestFullScreen();
			$doc.addEventListener(
				"mozfullscreenchange",
				function() {
					graphics.@org.parallax3d.parallax.platforms.gwt.GwtRendering::fullscreenChanged()();
				}, false);
			return true;
		}
		if (element.msRequestFullscreen) {
			element.width = $wnd.screen.width;
			element.height = $wnd.screen.height;
			element.msRequestFullscreen();
			$doc.addEventListener(
				"msfullscreenchange",
				function() {
					graphics.@org.parallax3d.parallax.platforms.gwt.GwtRendering::fullscreenChanged()();
				}, false);
			return true;
		}

		return false;
	}-*/;

	private native void exitFullscreen () /*-{
		if ($doc.exitFullscreen)
			$doc.exitFullscreen();
		if ($doc.msExitFullscreen)
			$doc.msExitFullscreen();
		if ($doc.webkitExitFullscreen)
			$doc.webkitExitFullscreen();
		if ($doc.mozExitFullscreen)
			$doc.mozExitFullscreen();
		if ($doc.webkitCancelFullScreen) // Old WebKit
			$doc.webkitCancelFullScreen();
	}-*/;

	@Override
	public boolean supportsExtension (String extension) {
		if (extensions == null) extensions = App.gl.glGetString(GL20.GL_EXTENSIONS);
		return extensions.contains(extension);
	}

	public void update () {
		long currTimeStamp = System.currentTimeMillis();
		deltaTime = (currTimeStamp - lastTimeStamp) / 1000.0f;
		lastTimeStamp = currTimeStamp;
		time += deltaTime;
		frames++;
		if (time > 1) {
			this.fps = frames;
			time = 0;
			frames = 0;
		}
	}

	@Override
	public float getRawDeltaTime () {
		return getDeltaTime();
	}

	@Override
	public boolean isFullscreen () {
		return isFullscreenJSNI();
	}

	@Override
	public boolean isGL30Available () {
		return false;
	}
}
