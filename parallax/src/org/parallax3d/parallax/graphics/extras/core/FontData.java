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

public class FontData {

    public static abstract class GliphAction {
        public double x;
        public double y;

        @Override
        public String toString() {
            return " {x=" + x + ", y=" + y + "}";
        }
    }

    public static class GliphActionMoveTo extends GliphAction { }

    public static class GliphActionlineTo extends GliphAction { }

    public static class GliphActionQuadraticCurveTo extends GliphAction {
        public double x1;
        public double y1;
    }

    public static class GliphActionBezierCurveTo extends GliphAction {
        public double x1;
        public double y1;
        public double x2;
        public double y2;
    }

    public static class Glyph {
        public List<GliphAction> actions;
        public double ha;

        @Override
        public String toString() {
            String retval = "";
            for(GliphAction act: actions)
                retval += act + ", ";
            return "[" + retval + "]";
        }
    }

    public double resolution;

    public FastMap<Glyph> gliphs;

}
