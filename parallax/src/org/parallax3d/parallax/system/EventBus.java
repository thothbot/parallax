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

import org.parallax3d.parallax.system.events.Event;
import org.parallax3d.parallax.system.events.EventListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EventBus {

    private static final EventDispatcher eventDispatcher = new EventDispatcher();

    private static Map<Class<? extends EventListener>, LinkedList<WeakReference<EventListener>>> listeners = new HashMap<>();

    public static void addEventListener(EventListener listener)
    {
        eventDispatcher.addEventListener( listener );
    }

    public static boolean hasEventListener(EventListener listener)
    {
        return eventDispatcher.hasEventListener( listener );
    }

    public static void removeEventListener(EventListener listener)
    {
        eventDispatcher.removeEventListener( listener );
    }

    public static void dispatchEvent(Event event)
    {
        eventDispatcher.dispatchEvent( event );
    }
}
