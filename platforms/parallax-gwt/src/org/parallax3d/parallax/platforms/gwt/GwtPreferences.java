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

package org.parallax3d.parallax.platforms.gwt;

import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.Preferences;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ParallaxRuntimeException;

public class GwtPreferences implements Preferences {

    final String prefix;
    FastMap<Object> values = new FastMap<Object>();

    GwtPreferences (String prefix) {
        this.prefix = prefix + ":";
        int prefixLength = this.prefix.length();
        try {
            for (int i = 0; i < ((GwtParallax)Parallax.getInstance()).LocalStorage.getLength(); i++) {
                String key = ((GwtParallax)Parallax.getInstance()).LocalStorage.key(i);
                if (key.startsWith(prefix)) {
                    String value = ((GwtParallax)Parallax.getInstance()).LocalStorage.getItem(key);
                    values.put(key.substring(prefixLength, key.length() - 1), toObject(key, value));
                }
            }
        } catch (Exception e) {
            values.clear();
        }
    }

    @Override
    public Preferences put(String key, boolean val) {
        values.put(key, val);
        return this;
    }

    @Override
    public Preferences put(String key, int val) {
        values.put(key, val);
        return this;
    }

    @Override
    public Preferences put(String key, long val) {
        values.put(key, val);
        return this;
    }

    @Override
    public Preferences put(String key, float val) {
        values.put(key, val);
        return this;
    }

    @Override
    public Preferences put(String key, String val) {
        values.put(key, val);
        return this;
    }

    @Override
    public Preferences put(FastMap<?> vals) {
        for (String key : vals.keySet()) {
            values.put(key, vals.get(key));
        }
        return this;
    }

    @Override
    public boolean getBoolean(String key) {
        Boolean v = (Boolean)values.get(key);
        return v == null ? false : v;
    }

    @Override
    public int getInteger(String key) {
        Integer v = (Integer)values.get(key);
        return v == null ? 0 : v;
    }

    @Override
    public long getLong(String key) {
        Long v = (Long)values.get(key);
        return v == null ? 0 : v;
    }

    @Override
    public float getFloat(String key) {
        Float v = (Float)values.get(key);
        return v == null ? 0 : v;
    }

    @Override
    public String getString(String key) {
        String v = (String)values.get(key);
        return v == null ? "" : v;
    }

    @Override
    public FastMap<?> get() {
        FastMap<Object> map = new FastMap<Object>();
        for (String key : values.keySet()) {
            map.put(key, values.get(key));
        }
        return map;
    }

    @Override
    public boolean contains(String key) {
        return values.containsKey(key);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public void remove(String key) {
        values.remove(key);
    }

    @Override
    public void flush() {
        try {
            // remove all old values
            for (int i = 0; i < ((GwtParallax)Parallax.getInstance()).LocalStorage.getLength(); i++) {
                String key = ((GwtParallax)Parallax.getInstance()).LocalStorage.key(i);
                if (key.startsWith(prefix)) ((GwtParallax)Parallax.getInstance()).LocalStorage.removeItem(key);
            }

            // push new values to LocalStorage
            for (String key : values.keySet()) {
                String storageKey = toStorageKey(key, values.get(key));
                String storageValue = "" + values.get(key).toString();
                ((GwtParallax)Parallax.getInstance()).LocalStorage.setItem(storageKey, storageValue);
            }

        } catch (Exception e) {
            throw new ParallaxRuntimeException("Couldn't flush preferences");
        }
    }


    private Object toObject (String key, String value) {
        if (key.endsWith("b")) return new Boolean(Boolean.parseBoolean(value));
        if (key.endsWith("i")) return new Integer(Integer.parseInt(value));
        if (key.endsWith("l")) return new Long(Long.parseLong(value));
        if (key.endsWith("f")) return new Float(Float.parseFloat(value));
        return value;
    }

    private String toStorageKey (String key, Object value) {
        if (value instanceof Boolean) return prefix + key + "b";
        if (value instanceof Integer) return prefix + key + "i";
        if (value instanceof Long) return prefix + key + "l";
        if (value instanceof Float) return prefix + key + "f";
        return prefix + key + "s";
    }
}
