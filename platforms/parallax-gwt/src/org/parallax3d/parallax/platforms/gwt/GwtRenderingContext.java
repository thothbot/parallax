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

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.webgl.client.WebGLContextAttributes;
import com.google.gwt.webgl.client.WebGLRenderingContext;
import org.parallax3d.parallax.*;
import org.parallax3d.parallax.system.AnimationReadyListener;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.input.InputHandler;
import org.parallax3d.parallax.system.ParallaxRuntimeException;
import org.parallax3d.parallax.system.ViewportResizeBus;
import org.parallax3d.parallax.system.gl.GL20;

import java.util.ArrayList;
import java.util.List;

public class GwtRenderingContext implements ResizeHandler, RenderingContext, AnimationScheduler.AnimationCallback {

    /**
     * The ID of the pending animation request.
     */
    private AnimationScheduler.AnimationHandle animationHandle;

    Panel root;

    CanvasElement canvas;
    WebGLRenderingContext context;
    GLRenderer renderer;
    GL20 gl;

    GwtInput input;

    Animation listener;

    List<AnimationReadyListener> animationReadyListener = new ArrayList<>();

    int lastWidth;
    int lastHeight;

    double fps = 0;
    long lastTimeStamp = System.currentTimeMillis();
    long frameId = -1;
    double deltaTime = 0;
    double time = 0;
    int frames;

    GwtRenderingContextConfiguration config;

    public GwtRenderingContext(Panel root) throws ParallaxRuntimeException {
        this(root, new GwtRenderingContextConfiguration());
    }

    public GwtRenderingContext(Panel root, GwtRenderingContextConfiguration config) throws ParallaxRuntimeException {
        this.root = root;
        root.clear();

        Canvas canvasWidget = Canvas.createIfSupported();
        if (canvasWidget == null)
            throw new ParallaxRuntimeException("Canvas not supported");

        int width = root.getOffsetWidth();
        int height = root.getOffsetHeight();

        if (width == 0 || height == 0)
            new ParallaxRuntimeException("Width or Height of the Panel is 0");

        lastWidth = width;
        lastHeight = height;

        canvas = canvasWidget.getCanvasElement();
        root.add(canvasWidget);
        canvas.setWidth(width);
        canvas.setHeight(height);
        this.config = config;

        WebGLContextAttributes attributes = WebGLContextAttributes.create();
        attributes.setAntialias(config.isAntialiasing());
        attributes.setStencil(config.isStencil());
        attributes.setAlpha(config.isAlpha());
        attributes.setPremultipliedAlpha(config.isPremultipliedAlpha());
        attributes.setPreserveDrawingBuffer(config.isPreserveDrawingBuffer());

        context = WebGLRenderingContext.getContext(canvas, attributes);
        context.viewport(0, 0, width, height);

        gl = new GwtGL20(context);

        renderer = new GLRenderer(gl, width, height);

        input = new GwtInput( canvas );

        addEventListeners();

        Window.addResizeHandler( this );
    }

    @Override
    public void setAnimation(Animation animation) {
        this.listener = animation;
        // tell listener about app creation
        try {
            renderer.setDefaultGLState();

            if(listener instanceof InputHandler)
                input.addInputHandler((InputHandler) listener);
            listener.onStart(this);
            listener.onResize(this);

            for (AnimationReadyListener ready : animationReadyListener)
                ready.onAnimationReady(listener);

        } catch (Throwable t) {
            Log.error("GwtRendering: exception: " + t.getMessage(), t);
            t.printStackTrace();
            throw new ParallaxRuntimeException(t);
        }

        run();
    }

    private void refresh() {
        long currTimeStamp = System.currentTimeMillis();
        deltaTime = (currTimeStamp - lastTimeStamp) / 1000.0;
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
    public void execute(double timestamp) {
        try {
            mainLoop();
        } catch (Throwable t) {
            Log.error("GwtApplication: exception: " + t.getMessage(), t);
            throw new ParallaxRuntimeException(t);
        }

        animationHandle = AnimationScheduler.get().requestAnimationFrame(this, canvas);
    }

    private void mainLoop() {

        refresh();

        frameId++;
        this.listener.onUpdate(this);

    }

    @Override
    public void onResize(ResizeEvent resizeEvent) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {

                lastWidth = getWidth();
                lastHeight = getHeight();

                if(lastWidth > 0 && lastHeight > 0) {

                    if(!GwtRenderingContext.this.isRun())
                        GwtRenderingContext.this.run();

                    ViewportResizeBus.onViewportResize(lastWidth, lastHeight);

                    GwtRenderingContext.this.canvas.setWidth(lastWidth);
                    GwtRenderingContext.this.canvas.setHeight(lastHeight);
                    GwtRenderingContext.this.renderer.setSize(lastWidth, lastHeight);
                    GwtRenderingContext.this.listener.onResize(GwtRenderingContext.this);

                }
                else
                {
                    GwtRenderingContext.this.stop();
                }
            }
        });
    }

    @Override
    public void stop() {
        // Cancel the animation request.
        if (this.animationHandle != null) {
            this.animationHandle.cancel();
            this.animationHandle = null;
        }
    }

    @Override
    public void run() {

        stop();
        // Execute the first callback.
        AnimationScheduler.get().requestAnimationFrame(this, this.canvas);
    }

    @Override
    public boolean isRun() {
        return (this.animationHandle != null);
    }

    private native void addEventListeners () /*-{
        var self = this;
        $doc.addEventListener('visibilitychange', function (e) {
            self.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::onVisibilityChange(Z)($doc['hidden'] !== true);
        });
    }-*/;

    private void onVisibilityChange (boolean visible) {
        if (visible) {
            run();
        } else {
            stop();
        }
    }

    @Override
    public void addAnimationReadyListener(AnimationReadyListener animationReadyListener) {
        this.animationReadyListener.add(animationReadyListener);
    }

    public WebGLRenderingContext getContext() {
        return context;
    }

    @Override
    public GLRenderer getRenderer() {
        return renderer;
    }

    @Override
    public Input getInput () {
        return input;
    }

    @Override
    public GL20 getGL20() {
        return gl;
    }

    @Override
    public int getWidth() {
        return root.getOffsetWidth();
    }

    @Override
    public int getHeight() {
        return root.getOffsetHeight();
    }

    @Override
    public double getAspectRation() {
        return getWidth() / (double)getHeight();
    }

    @Override
    public long getFrameId() {
        return frameId;
    }

    @Override
    public double getDeltaTime() {
        return deltaTime;
    }

    @Override
    public int getFramesPerSecond() {
        return (int) fps;
    }

    @Override
    public float getPpiX() {
        return 96;
    }

    @Override
    public float getPpiY() {
        return 96;
    }

    @Override
    public float getDensity() {
        return 96.0f / 160;
    }

    @Override
    public double getRawDeltaTime() {
        return getDeltaTime();
    }

    @Override
    public boolean supportsDisplayModeChange() {
        return supportsFullscreenJSNI();
    }

    private native boolean supportsFullscreenJSNI() /*-{
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

    private native int getScreenWidthJSNI() /*-{
        return $wnd.screen.width;
    }-*/;

    private native int getScreenHeightJSNI() /*-{
        return $wnd.screen.height;
    }-*/;

    private native void exitFullscreen() /*-{
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

    private void fullscreenChanged() {
        if (!isFullscreen()) {
            renderer.setSize(lastWidth, lastHeight);
        }
    }

    @Override
    public void setFullscreen() {
        if (isFullscreenJSNI())
            exitFullscreen();
        else
            setFullscreenJSNI(this, canvas);
    }

    private native boolean setFullscreenJSNI(GwtRenderingContext graphics, CanvasElement element) /*-{
        // Attempt to use the non-prefixed standard API (https://fullscreen.spec.whatwg.org)
        if (element.requestFullscreen) {
            element.width = $wnd.screen.width;
            element.height = $wnd.screen.height;
            element.requestFullscreen();
            $doc.addEventListener(
                "fullscreenchange",
                function () {
                    graphics.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::fullscreenChanged()();
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
                function () {
                    graphics.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::fullscreenChanged()();
                }, false);
            return true;
        }
        if (element.mozRequestFullScreen) {
            element.width = $wnd.screen.width;
            element.height = $wnd.screen.height;
            element.mozRequestFullScreen();
            $doc.addEventListener(
                "mozfullscreenchange",
                function () {
                    graphics.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::fullscreenChanged()();
                }, false);
            return true;
        }
        if (element.msRequestFullscreen) {
            element.width = $wnd.screen.width;
            element.height = $wnd.screen.height;
            element.msRequestFullscreen();
            $doc.addEventListener(
                "msfullscreenchange",
                function () {
                    graphics.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::fullscreenChanged()();
                }, false);
            return true;
        }

        return false;
    }-*/;

    @Override
    public boolean isFullscreen() {
        return isFullscreenJSNI();
    }

    private native boolean isFullscreenJSNI() /*-{
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
}
