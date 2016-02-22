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

package org.parallax3d.parallax.system.jsonbind.vm.json;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JSONArray {
    private final List<Object> values;

    public JSONArray() {
        this.values = new ArrayList();
    }

    public JSONArray(Collection var1) {
        this();
        if(var1 != null) {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
                this.put(JSONObject.wrap(var2.next()));
            }
        }

    }

    public JSONArray(JSONTokener var1) throws JSONException {
        Object var2 = var1.nextValue();
        if(var2 instanceof JSONArray) {
            this.values = ((JSONArray)var2).values;
        } else {
            throw JSON.typeMismatch(var2, "JSONArray");
        }
    }

    public JSONArray(String var1) throws JSONException {
        this(new JSONTokener(var1));
    }

    public JSONArray(Object var1) throws JSONException {
        if(!var1.getClass().isArray()) {
            throw new JSONException("Not a primitive array: " + var1.getClass());
        } else {
            int var2 = Array.getLength(var1);
            this.values = new ArrayList(var2);

            for(int var3 = 0; var3 < var2; ++var3) {
                this.put(JSONObject.wrap(Array.get(var1, var3)));
            }

        }
    }

    public int length() {
        return this.values.size();
    }

    public JSONArray put(boolean var1) {
        this.values.add(Boolean.valueOf(var1));
        return this;
    }

    public JSONArray put(double var1) throws JSONException {
        this.values.add(Double.valueOf(JSON.checkDouble(var1)));
        return this;
    }

    public JSONArray put(int var1) {
        this.values.add(Integer.valueOf(var1));
        return this;
    }

    public JSONArray put(long var1) {
        this.values.add(Long.valueOf(var1));
        return this;
    }

    public JSONArray put(Object var1) {
        this.values.add(var1);
        return this;
    }

    public JSONArray put(int var1, boolean var2) throws JSONException {
        return this.put(var1, Boolean.valueOf(var2));
    }

    public JSONArray put(int var1, double var2) throws JSONException {
        return this.put(var1, Double.valueOf(var2));
    }

    public JSONArray put(int var1, int var2) throws JSONException {
        return this.put(var1, Integer.valueOf(var2));
    }

    public JSONArray put(int var1, long var2) throws JSONException {
        return this.put(var1, Long.valueOf(var2));
    }

    public JSONArray put(int var1, Object var2) throws JSONException {
        if(var2 instanceof Number) {
            JSON.checkDouble(((Number)var2).doubleValue());
        }

        while(this.values.size() <= var1) {
            this.values.add((Object)null);
        }

        this.values.set(var1, var2);
        return this;
    }

    public boolean isNull(int var1) {
        Object var2 = this.opt(var1);
        return var2 == null || var2 == JSONObject.NULL;
    }

    public Object get(int var1) throws JSONException {
        try {
            Object var2 = this.values.get(var1);
            if(var2 == null) {
                throw new JSONException("Value at " + var1 + " is null.");
            } else {
                return var2;
            }
        } catch (IndexOutOfBoundsException var3) {
            throw new JSONException("Index " + var1 + " out of range [0.." + this.values.size() + ")");
        }
    }

    public Object opt(int var1) {
        return var1 >= 0 && var1 < this.values.size()?this.values.get(var1):null;
    }

    public Object remove(int var1) {
        return var1 >= 0 && var1 < this.values.size()?this.values.remove(var1):null;
    }

    public boolean getBoolean(int var1) throws JSONException {
        Object var2 = this.get(var1);
        Boolean var3 = JSON.toBoolean(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(Integer.valueOf(var1), var2, "boolean");
        } else {
            return var3.booleanValue();
        }
    }

    public boolean optBoolean(int var1) {
        return this.optBoolean(var1, false);
    }

    public boolean optBoolean(int var1, boolean var2) {
        Object var3 = this.opt(var1);
        Boolean var4 = JSON.toBoolean(var3);
        return var4 != null?var4.booleanValue():var2;
    }

    public double getDouble(int var1) throws JSONException {
        Object var2 = this.get(var1);
        Double var3 = JSON.toDouble(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(Integer.valueOf(var1), var2, "double");
        } else {
            return var3.doubleValue();
        }
    }

    public double optDouble(int var1) {
        return this.optDouble(var1, 0.0D / 0.0);
    }

    public double optDouble(int var1, double var2) {
        Object var4 = this.opt(var1);
        Double var5 = JSON.toDouble(var4);
        return var5 != null?var5.doubleValue():var2;
    }

    public int getInt(int var1) throws JSONException {
        Object var2 = this.get(var1);
        Integer var3 = JSON.toInteger(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(Integer.valueOf(var1), var2, "int");
        } else {
            return var3.intValue();
        }
    }

    public int optInt(int var1) {
        return this.optInt(var1, 0);
    }

    public int optInt(int var1, int var2) {
        Object var3 = this.opt(var1);
        Integer var4 = JSON.toInteger(var3);
        return var4 != null?var4.intValue():var2;
    }

    public long getLong(int var1) throws JSONException {
        Object var2 = this.get(var1);
        Long var3 = JSON.toLong(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(Integer.valueOf(var1), var2, "long");
        } else {
            return var3.longValue();
        }
    }

    public long optLong(int var1) {
        return this.optLong(var1, 0L);
    }

    public long optLong(int var1, long var2) {
        Object var4 = this.opt(var1);
        Long var5 = JSON.toLong(var4);
        return var5 != null?var5.longValue():var2;
    }

    public String getString(int var1) throws JSONException {
        Object var2 = this.get(var1);
        String var3 = JSON.toString(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(Integer.valueOf(var1), var2, "String");
        } else {
            return var3;
        }
    }

    public String optString(int var1) {
        return this.optString(var1, "");
    }

    public String optString(int var1, String var2) {
        Object var3 = this.opt(var1);
        String var4 = JSON.toString(var3);
        return var4 != null?var4:var2;
    }

    public JSONArray getJSONArray(int var1) throws JSONException {
        Object var2 = this.get(var1);
        if(var2 instanceof JSONArray) {
            return (JSONArray)var2;
        } else {
            throw JSON.typeMismatch(Integer.valueOf(var1), var2, "JSONArray");
        }
    }

    public JSONArray optJSONArray(int var1) {
        Object var2 = this.opt(var1);
        return var2 instanceof JSONArray?(JSONArray)var2:null;
    }

    public JSONObject getJSONObject(int var1) throws JSONException {
        Object var2 = this.get(var1);
        if(var2 instanceof JSONObject) {
            return (JSONObject)var2;
        } else {
            throw JSON.typeMismatch(Integer.valueOf(var1), var2, "JSONObject");
        }
    }

    public JSONObject optJSONObject(int var1) {
        Object var2 = this.opt(var1);
        return var2 instanceof JSONObject?(JSONObject)var2:null;
    }

    public JSONObject toJSONObject(JSONArray var1) throws JSONException {
        JSONObject var2 = new JSONObject();
        int var3 = Math.min(var1.length(), this.values.size());
        if(var3 == 0) {
            return null;
        } else {
            for(int var4 = 0; var4 < var3; ++var4) {
                String var5 = JSON.toString(var1.opt(var4));
                var2.put(var5, this.opt(var4));
            }

            return var2;
        }
    }

    public String join(String var1) throws JSONException {
        JSONStringer var2 = new JSONStringer();
        var2.open(JSONStringer.Scope.NULL, "");
        int var3 = 0;

        for(int var4 = this.values.size(); var3 < var4; ++var3) {
            if(var3 > 0) {
                var2.out.append(var1);
            }

            var2.value(this.values.get(var3));
        }

        var2.close(JSONStringer.Scope.NULL, JSONStringer.Scope.NULL, "");
        return var2.out.toString();
    }

    public String toString() {
        try {
            JSONStringer var1 = new JSONStringer();
            this.writeTo(var1);
            return var1.toString();
        } catch (JSONException var2) {
            return null;
        }
    }

    public String toString(int var1) throws JSONException {
        JSONStringer var2 = new JSONStringer(var1);
        this.writeTo(var2);
        return var2.toString();
    }

    void writeTo(JSONStringer var1) throws JSONException {
        var1.array();
        Iterator var2 = this.values.iterator();

        while(var2.hasNext()) {
            Object var3 = var2.next();
            var1.value(var3);
        }

        var1.endArray();
    }

    public boolean equals(Object var1) {
        return var1 instanceof JSONArray && ((JSONArray)var1).values.equals(this.values);
    }

    public int hashCode() {
        return this.values.hashCode();
    }
}
