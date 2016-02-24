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

public abstract class ParallaxTest extends AnimationAdapter {

    /**
     * The name of the example.
     */
    public abstract String getName();

    /**
     * A description of an example.
     */
    public abstract String getDescription();

    public abstract String getAuthor();

    public boolean isEnabledEffectSwitch() {
        return true;
    }

    /**
     * Get an image of an example to show on the index page
     *
     * @return ImageResource
     */
    public String getIconUrl() {
        String icon = getTestName() + ".jpg";
        return "assets/thumbs/" + icon;
    }

    public String getTestGroupName() {
        String cls = this.getClass().getName();
        String name = cls.substring(0, cls.lastIndexOf(".") ).replace("org.parallax3d.parallax.tests.cases","");
        if(name.length() == 0)
            name = "Unspecified";
        else
            name = name.substring(1);
        return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
    }

    /**
     * Get the simple filename of a class (name without dots).
     */
    public String getTestName()
    {
        String name = this.getClass().getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
