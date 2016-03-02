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
import org.parallax3d.parallax.loaders.typefacejs.JsoFont;
import org.parallax3d.parallax.loaders.typefacejs.JsoFontFactory;
import org.parallax3d.parallax.system.jsonbind.AutoBean;
import org.parallax3d.parallax.system.jsonbind.AutoBeanCodex;
import org.parallax3d.parallax.system.jsonbind.JsonBindProxy;

public class TypefacejsLoader extends FontLoader {

    JsoFont font;

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
