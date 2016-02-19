package org.parallax3d.parallax;

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

import com.google.gwt.user.client.ui.Widget;
import org.parallax3d.parallax.graphics.renderers.CSS3DRenderer;
import org.parallax3d.parallax.graphics.scenes.Scene;


/**
 * The class to set up {@link Scene} for the {@link CSS3DRenderer}
 *
 * rebuild from AnimatedScene
 *
 * @author svv2014
 *
 */
public abstract class CSS3DAnimatedScene
{

    /**
     * Basically use for the debugger. Check if needed.
     *
     * @author thothbot
     *
     */
    public static interface AnimationUpdateHandler
    {
        /**
         * Called when onUpdate() called.
         */
        public void onUpdate(double duration);
    }

    private CSS3DRenderer renderingPanel = new CSS3DRenderer();
    private Scene scene = new Scene();
    private AnimationUpdateHandler animationUpdateHandler;

    /**
     * Gets the main {@link Scene} object.
     *
     * @return the Scene object.
     */
    public Scene getScene()
    {
        return this.scene;
    }

    /**
     * Gets {@link CSS3DRenderer} associated with the CSS3DAnimatedScene.
     *
     * @return the {@link CSS3DRenderer} instance.
     */
    public CSS3DRenderer getRenderer()
    {
        return this.renderingPanel;
    }

    public Widget getCanvas()
    {
        return this.renderingPanel.getDomElement();
    }

    /**
     * Initialize the scene.
     *
     * @param animationUpdateHandler this parameter used for updating debug info. Can be null.
     */
    public void init(AnimationUpdateHandler animationUpdateHandler)
    {
        this.animationUpdateHandler = animationUpdateHandler;
    }

    protected abstract void onUpdate(double duration);

    protected void onRefresh(double duration)
    {

        onUpdate(duration);
        animationUpdateHandler.onUpdate(duration);
    }
}
