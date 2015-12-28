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

package org.parallax3d.parallax;

import org.parallax3d.parallax.system.FastMap;

import java.util.Map;

public interface Preferences {
    public Preferences put(String key, boolean val);

    public Preferences put(String key, int val);

    public Preferences put(String key, long val);

    public Preferences put(String key, float val);

    public Preferences put(String key, String val);

    public Preferences put(FastMap<?> vals);

    public boolean getBoolean(String key);

    public int getInteger(String key);

    public long getLong(String key);

    public float getFloat(String key);

    public String getString(String key);

    public FastMap<?> get();

    public boolean contains(String key);

    public void clear();

    public void remove(String key);

    public void flush ();
}
