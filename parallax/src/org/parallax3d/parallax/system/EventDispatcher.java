/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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
package org.parallax3d.parallax.system;

import org.parallax3d.parallax.system.events.*;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

@ThreejsObject("THREE.EventDispatcher")
public class EventDispatcher {

    private static Map<Class<? extends EventListener>, LinkedList<WeakReference<EventListener>>> listeners = new HashMap<>();

    public static void addEventListener(EventListener listener)
    {
        Class<? extends EventListener> key = listener.getClass();

        if(!listeners.containsKey(key))
            listeners.put(key, new LinkedList<WeakReference<EventListener>>());

        if (!hasEventListener(listener))
            listeners.get(key).add(new WeakReference<>(listener));
    }

    public static boolean hasEventListener(EventListener listener)
    {
        Class<? extends EventListener> key = listener.getClass();

        if(!listeners.containsKey(key))
            return false;

        for(WeakReference<EventListener> l: listeners.get(key))
        {
            if (l.get() == listener)
                return true;
        }

        return false;
    }

    public static void removeEventListener(EventListener listener)
    {
        if(!hasEventListener(listener))
            return;

        Class<? extends EventListener> key = listener.getClass();

        Iterator<WeakReference<EventListener>> it = listeners.get(key).iterator();
        while (it.hasNext())
        {
            WeakReference<EventListener> element = it.next();
            if (element.get() == null || element.get() == listener)
            {
                it.remove();
            }
        }
    }

    public static void dispatchEvent(Event event)
    {
        Class<? extends EventListener> key = event.getListener();

        if(!listeners.containsKey(key))
            return;

        Iterator<WeakReference<EventListener>> it = listeners.get(key).iterator();
        while (it.hasNext())
        {
            WeakReference<EventListener> element = it.next();
            if (element.get() == null)
                it.remove();
            else
                event.dispatch(element.get());
        }
    }
}
