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

package org.parallax3d.parallax.graphics.extras.core;

import org.parallax3d.parallax.system.FastMap;

import java.util.List;

public interface Font {

    interface Glyph {
        interface GliphAction {
            double getX();
            double getY();
        }

        interface GliphActionMoveTo extends GliphAction { }

        interface GliphActionlineTo extends GliphAction { }

        interface GliphActionQuadraticCurveTo extends GliphAction {
            double getX1();
            double getY1();
        }

        interface GliphActionBezierCurveTo extends GliphAction {
            double getX1();
            double getY1();
            double getX2();
            double getY2();
        }

        List<GliphAction> getAction();
        double getHa();
    }

    double getResolution();

    FastMap<Glyph> getGlyphs();

}
