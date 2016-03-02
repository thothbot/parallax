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

import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.TextGeometry")
public class TextGeometry extends ExtrudeGeometry {

    String text;
    double size;

    Font font;

    public TextGeometry(String text, Font font) {
        this(text, font, 100);
    }

    public TextGeometry(String text, Font font, double size) {
        this(text, font, size, new ExtrudeGeometryParameters());
    }

    public TextGeometry(String text, Font font, double size, ExtrudeGeometryParameters parameters) {
        super(parameters);

        this.text = text;
        this.size = size;
        this.font = font;

        update();
    }

    public String getText() {
        return text;
    }

    public TextGeometry setText(String text) {
        this.text = text;

        return this;
    }

    public Font getFont() {
        return font;
    }

    public TextGeometry setFont(Font font) {
        this.font = font;

        return this;
    }

    public double getSize() {
        return size;
    }

    public TextGeometry setSize(double size) {
        this.size = size;

        return this;
    }

    public void update() {
        String[] chars = text.split("");
        int length = chars.length;

        double scale = size / font.getResolution();

        double offset = 0;

        for ( int i = 0; i < length; i ++ ) {

            String c = chars[i];
            Path path = new Path();

            Font.Glyph glyph = font.getGlyphs().containsKey(c) ? font.getGlyphs().get(c) : font.getGlyphs().get("?");

            for(Font.Glyph.GliphAction action : glyph.getAction())
            {
                if(action instanceof Font.Glyph.GliphActionMoveTo)
                {
                    path.moveTo( action.getX() * scale + offset, action.getY() * scale );
                }
                else if(action instanceof Font.Glyph.GliphActionlineTo)
                {
                    path.lineTo( action.getX() * scale + offset, action.getY() * scale );
                }
                else if(action instanceof Font.Glyph.GliphActionQuadraticCurveTo)
                {
                    path.quadraticCurveTo(
                            ((Font.Glyph.GliphActionQuadraticCurveTo)action).getX1() * scale + offset,
                            ((Font.Glyph.GliphActionQuadraticCurveTo)action).getY1() * scale ,
                            action.getX() * scale + offset,
                            action.getY() * scale );
                }
                else if(action instanceof Font.Glyph.GliphActionBezierCurveTo)
                {
                    path.bezierCurveTo(
                            ((Font.Glyph.GliphActionBezierCurveTo)action).getX1() * scale + offset,
                            ((Font.Glyph.GliphActionBezierCurveTo)action).getY1() * scale ,
                            ((Font.Glyph.GliphActionBezierCurveTo)action).getX2() * scale + offset,
                            ((Font.Glyph.GliphActionBezierCurveTo)action).getY2() * scale ,
                            action.getX() * scale + offset,
                            action.getY() * scale );
                }
            }

            addShape(path.toShapes(), this.options);
            offset = glyph.getHa() * scale;

        }

        double width = offset / 2.0;
    }
}
