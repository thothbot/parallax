/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 *
 * This file is part of the realh fork of the Parallax project.
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

package org.parallax3d.parallax.system;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

public class ViewportResizeBus {
    private static LinkedList<WeakReference<ViewportResizeListener>> listeners =
            new LinkedList<WeakReference<ViewportResizeListener>>();

    public static void addViewportResizeListener(ViewportResizeListener listener)
    {
        if (!contains(listener))
            listeners.add(new WeakReference<ViewportResizeListener>(listener));
    }

    public static void removeViewportResizeListener(ViewportResizeListener listener)
    {
        Iterator<WeakReference<ViewportResizeListener>> it = listeners.iterator();
        while (it.hasNext())
        {
            WeakReference<ViewportResizeListener> element = it.next();
            if (element.get() == null || element.get() == listener)
            {
                it.remove();
            }
        }
    }

    public static void onViewportResize(int newWidth, int newHeight)
    {
        Iterator<WeakReference<ViewportResizeListener>> it = listeners.iterator();
        while (it.hasNext())
        {
            WeakReference<ViewportResizeListener> element = it.next();
            if (element.get() == null)
                it.remove();
            else
                element.get().onViewportResize(newWidth, newHeight);
        }
    }

    private static boolean contains(ViewportResizeListener listener)
    {
        for(WeakReference<ViewportResizeListener> l: listeners)
        {
            if (l.get() == listener)
                return true;
        }
        return false;
    }

}
