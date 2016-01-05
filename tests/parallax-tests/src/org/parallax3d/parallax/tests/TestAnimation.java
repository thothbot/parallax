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

package org.parallax3d.parallax.tests;

import org.parallax3d.parallax.AnimationAdapter;

public abstract class TestAnimation extends AnimationAdapter {

    /**
     * The name of the example.
     */
    public abstract String getName();

    /**
     * A description of an example.
     */
    public abstract String getDescription();

    public boolean isEnabledEffectSwitch() {
        return true;
    }

    /**
     * Get an image of an example to show on the index page
     *
     * @return ImageResource
     */
    public String getIconUrl() {
        String icon = getSimpleName(this.getClass()) + ".jpg";
        return "assets/thumbs/" + icon;
    }

    /**
     * Get the token for a given content widget.
     *
     * @return the content widget token.
     */
    public String getContentWidgetToken()
    {
        return getSimpleName(this.getClass());
    }

    /**
     * Get the simple filename of a class (name without dots).
     *
     * @param c
     *            the class
     */
    protected static String getSimpleName(Class<?> c)
    {
        String name = c.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
