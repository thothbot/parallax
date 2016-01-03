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

package org.parallax3d.parallax.system;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class FastMap<V> extends AbstractMap<String, V> implements Serializable {

  private transient HashMap<String, V> javaMap;

  public FastMap() {
      javaMap = new HashMap<String, V>();
  }

  @Override
  public void clear() {
      javaMap.clear();
  }

  @Override
  public boolean containsKey(Object key) {

      return javaMap.containsKey(key);

  }

  @Override
  public boolean containsValue(Object value) {
    return values().contains(value);
  }

  @Override
  public Set<Entry<String, V>> entrySet() {

      return javaMap.entrySet();

  }

  @Override
  public V get(Object key) {

      return javaMap.get(key);

  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public Set<String> keySet() {

      return javaMap.keySet();

  }

  @Override
  public V put(String key, V value) {

      return javaMap.put(key, value);

  }

  @Override
  public void putAll(Map<? extends String, ? extends V> m) {

      javaMap.putAll(m);

  }

  @Override
  public V remove(Object key) {

      return javaMap.remove(key);

  }

  @Override
  public int size() {

      return javaMap.size();

  }

  @Override
  public Collection<V> values() {

      return javaMap.values();

  }
}
