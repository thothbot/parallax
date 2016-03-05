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

    FontData font;

    public TextGeometry(String text, FontData font) {
        this(text, font, 100);
    }

    public TextGeometry(String text, FontData font, double size) {
        this(text, font, size, new ExtrudeGeometryParameters());
    }

    public TextGeometry(String text, FontData font, double size, ExtrudeGeometryParameters parameters) {
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

    public FontData getFont() {
        return font;
    }

    public TextGeometry setFont(FontData font) {
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
        char[] chars = text.toCharArray();
        int length = chars.length;

        double scale = size / font.resolution;

        double offset = 0;

        for ( int i = 0; i < length; i ++ ) {

            char c = chars[i];
            Path path = new Path();

            FontData.Glyph glyph = font.gliphs.containsKey(c) ? font.gliphs.get(c) : font.gliphs.get("?");
            for(FontData.GliphAction action : glyph.actions)
            {
                if(action instanceof FontData.GliphActionMoveTo)
                {
                    path.moveTo( action.x * scale + offset, action.y * scale );
                }
                else if(action instanceof FontData.GliphActionlineTo)
                {
                    path.lineTo( action.x * scale + offset, action.y * scale );
                }
                else if(action instanceof FontData.GliphActionQuadraticCurveTo)
                {
                    path.quadraticCurveTo(
                            ((FontData.GliphActionQuadraticCurveTo)action).x1 * scale + offset,
                            ((FontData.GliphActionQuadraticCurveTo)action).y1 * scale ,
                            action.x * scale + offset,
                            action.y * scale );
                }
                else if(action instanceof FontData.GliphActionBezierCurveTo)
                {
                    path.bezierCurveTo(
                            ((FontData.GliphActionBezierCurveTo)action).x1 * scale + offset,
                            ((FontData.GliphActionBezierCurveTo)action).y1 * scale ,
                            ((FontData.GliphActionBezierCurveTo)action).x2 * scale + offset,
                            ((FontData.GliphActionBezierCurveTo)action).y2 * scale ,
                            action.x * scale + offset,
                            action.y * scale );
                }
            }

            addShape(path.toShapes(), this.options);
            offset += glyph.ha * scale;

        }
    }
}
