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

package org.parallax3d.parallax.loaders;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.graphics.extras.core.FontData;
import org.parallax3d.parallax.loaders.typefacejs.JsoFont;
import org.parallax3d.parallax.loaders.typefacejs.JsoFontFactory;
import org.parallax3d.parallax.loaders.typefacejs.JsoGlyph;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.jsonbind.AutoBean;
import org.parallax3d.parallax.system.jsonbind.AutoBeanCodex;
import org.parallax3d.parallax.system.jsonbind.JsonBindProxy;

import java.util.ArrayList;
import java.util.Map;

public class TypefacejsLoader extends FontLoader {

    JsoFont font;
    FontData fontData;

    public TypefacejsLoader(String url, FontLoadHandler modelLoadHandler)
    {
        super(url, modelLoadHandler);
    }

    @Override
    protected void parse(FileHandle result) {
        if(!isValidTypefacejs(result.readString()))
            return;


        Log.debug("TypefacejsLoader parse()");
    }

    @Override
    public FontData getFontData()
    {
        if(fontData == null)
        {
            fontData = new FontData();
            fontData.resolution = font.getResolution();
            fontData.gliphs = new FastMap<>();

            for(Map.Entry<String, JsoGlyph> entry: font.getGlyphs().entrySet()){
                if(entry.getKey() == null || entry.getValue().getO() == null)
                    continue;

                FontData.Glyph gliph = new FontData.Glyph();
                gliph.ha = entry.getValue().getHa();
                gliph.actions = new ArrayList<>();

                String outline[] = entry.getValue().getO().split(" ");
                for(int i =0; i<outline.length;)
                {
                    String act = outline[ i ++ ];
                    FontData.GliphAction action = null;

                    switch ( act ) {

                        case "m": // Move To
                            action = new FontData.GliphActionMoveTo();

                            action.x = Double.parseDouble(outline[ i ++ ]);
                            action.y = Double.parseDouble(outline[ i ++ ]);

                            break;

                        case "l": // Line To
                            action = new FontData.GliphActionlineTo();

                            action.x = Double.parseDouble(outline[ i ++ ]);
                            action.y = Double.parseDouble(outline[ i ++ ]);

                            break;

                        case "q": // QuadraticCurveTo
                            action = new FontData.GliphActionQuadraticCurveTo();

                            action.x  = Double.parseDouble(outline[ i ++ ]);
                            action.y  = Double.parseDouble(outline[ i ++ ]);
                            ((FontData.GliphActionQuadraticCurveTo)action).x1 = Double.parseDouble(outline[ i ++ ]);
                            ((FontData.GliphActionQuadraticCurveTo)action).y1 = Double.parseDouble(outline[ i ++ ]);

                            break;

                        case "b": // Cubic Bezier Curve
                            action = new FontData.GliphActionBezierCurveTo();

                            action.x  = Double.parseDouble(outline[ i ++ ]);
                            action.y  = Double.parseDouble(outline[ i ++ ]);
                            ((FontData.GliphActionBezierCurveTo)action).x1 = Double.parseDouble(outline[ i ++ ]);
                            ((FontData.GliphActionBezierCurveTo)action).y1 = Double.parseDouble(outline[ i ++ ]);
                            ((FontData.GliphActionBezierCurveTo)action).x2 = Double.parseDouble(outline[ i ++ ]);
                            ((FontData.GliphActionBezierCurveTo)action).y2 = Double.parseDouble(outline[ i ++ ]);

                            break;

                    }

                    if(action != null)
                        gliph.actions.add(action);
                }

                fontData.gliphs.put(entry.getKey(), gliph);
            }
        }

        return fontData;
    }

    private boolean isValidTypefacejs(String string)
    {
        string = string.replaceAll("(\\s*if.+loadFace\\()", "").replaceAll("(\\)\\s*\\;)", "");

        JsoFontFactory factory = JsonBindProxy.create(JsoFontFactory.class);

        try
        {
            AutoBean<JsoFont> bean = AutoBeanCodex.decode(factory, JsoFont.class, string);
            font = bean.as();
        }
        catch ( Exception e)
        {
            Log.error("Could not parser Typefacejs data");
            return false;
        }

        return true;
    }
}
