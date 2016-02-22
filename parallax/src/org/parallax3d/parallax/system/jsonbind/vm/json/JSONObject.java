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

import java.util.*;

public class JSONObject {
    private static final Double NEGATIVE_ZERO = Double.valueOf(-0.0D);
    public static final Object NULL = new Object() {
        public boolean equals(Object var1) {
            return var1 == this || var1 == null;
        }

        public String toString() {
            return "null";
        }
    };
    private final Map<String, Object> nameValuePairs;

    public JSONObject() {
        this.nameValuePairs = new HashMap();
    }

    public JSONObject(Map var1) {
        this();
        Iterator var3 = var1.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            if(var5 == null) {
                throw new NullPointerException("key == null");
            }

            this.nameValuePairs.put(var5, wrap(var4.getValue()));
        }

    }

    public JSONObject(JSONTokener var1) throws JSONException {
        Object var2 = var1.nextValue();
        if(var2 instanceof JSONObject) {
            this.nameValuePairs = ((JSONObject)var2).nameValuePairs;
        } else {
            throw JSON.typeMismatch(var2, "JSONObject");
        }
    }

    public JSONObject(String var1) throws JSONException {
        this(new JSONTokener(var1));
    }

    public JSONObject(JSONObject var1, String[] var2) throws JSONException {
        this();
        String[] var3 = var2;
        int var4 = var2.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            Object var7 = var1.opt(var6);
            if(var7 != null) {
                this.nameValuePairs.put(var6, var7);
            }
        }

    }

    public int length() {
        return this.nameValuePairs.size();
    }

    public JSONObject put(String var1, boolean var2) throws JSONException {
        this.nameValuePairs.put(this.checkName(var1), Boolean.valueOf(var2));
        return this;
    }

    public JSONObject put(String var1, double var2) throws JSONException {
        this.nameValuePairs.put(this.checkName(var1), Double.valueOf(JSON.checkDouble(var2)));
        return this;
    }

    public JSONObject put(String var1, int var2) throws JSONException {
        this.nameValuePairs.put(this.checkName(var1), Integer.valueOf(var2));
        return this;
    }

    public JSONObject put(String var1, long var2) throws JSONException {
        this.nameValuePairs.put(this.checkName(var1), Long.valueOf(var2));
        return this;
    }

    public JSONObject put(String var1, Object var2) throws JSONException {
        if(var2 == null) {
            this.nameValuePairs.remove(var1);
            return this;
        } else {
            if(var2 instanceof Number) {
                JSON.checkDouble(((Number)var2).doubleValue());
            }

            this.nameValuePairs.put(this.checkName(var1), var2);
            return this;
        }
    }

    public JSONObject putOpt(String var1, Object var2) throws JSONException {
        return var1 != null && var2 != null?this.put(var1, var2):this;
    }

    public JSONObject accumulate(String var1, Object var2) throws JSONException {
        Object var3 = this.nameValuePairs.get(this.checkName(var1));
        if(var3 == null) {
            return this.put(var1, var2);
        } else {
            if(var2 instanceof Number) {
                JSON.checkDouble(((Number)var2).doubleValue());
            }

            JSONArray var4;
            if(var3 instanceof JSONArray) {
                var4 = (JSONArray)var3;
                var4.put(var2);
            } else {
                var4 = new JSONArray();
                var4.put(var3);
                var4.put(var2);
                this.nameValuePairs.put(var1, var4);
            }

            return this;
        }
    }

    String checkName(String var1) throws JSONException {
        if(var1 == null) {
            throw new JSONException("Names must be non-null");
        } else {
            return var1;
        }
    }

    public Object remove(String var1) {
        return this.nameValuePairs.remove(var1);
    }

    public boolean isNull(String var1) {
        Object var2 = this.nameValuePairs.get(var1);
        return var2 == null || var2 == NULL;
    }

    public boolean has(String var1) {
        return this.nameValuePairs.containsKey(var1);
    }

    public Object get(String var1) throws JSONException {
        Object var2 = this.nameValuePairs.get(var1);
        if(var2 == null) {
            throw new JSONException("No value for " + var1);
        } else {
            return var2;
        }
    }

    public Object opt(String var1) {
        return this.nameValuePairs.get(var1);
    }

    public boolean getBoolean(String var1) throws JSONException {
        Object var2 = this.get(var1);
        Boolean var3 = JSON.toBoolean(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(var1, var2, "boolean");
        } else {
            return var3.booleanValue();
        }
    }

    public boolean optBoolean(String var1) {
        return this.optBoolean(var1, false);
    }

    public boolean optBoolean(String var1, boolean var2) {
        Object var3 = this.opt(var1);
        Boolean var4 = JSON.toBoolean(var3);
        return var4 != null?var4.booleanValue():var2;
    }

    public double getDouble(String var1) throws JSONException {
        Object var2 = this.get(var1);
        Double var3 = JSON.toDouble(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(var1, var2, "double");
        } else {
            return var3.doubleValue();
        }
    }

    public double optDouble(String var1) {
        return this.optDouble(var1, 0.0D / 0.0);
    }

    public double optDouble(String var1, double var2) {
        Object var4 = this.opt(var1);
        Double var5 = JSON.toDouble(var4);
        return var5 != null?var5.doubleValue():var2;
    }

    public int getInt(String var1) throws JSONException {
        Object var2 = this.get(var1);
        Integer var3 = JSON.toInteger(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(var1, var2, "int");
        } else {
            return var3.intValue();
        }
    }

    public int optInt(String var1) {
        return this.optInt(var1, 0);
    }

    public int optInt(String var1, int var2) {
        Object var3 = this.opt(var1);
        Integer var4 = JSON.toInteger(var3);
        return var4 != null?var4.intValue():var2;
    }

    public long getLong(String var1) throws JSONException {
        Object var2 = this.get(var1);
        Long var3 = JSON.toLong(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(var1, var2, "long");
        } else {
            return var3.longValue();
        }
    }

    public long optLong(String var1) {
        return this.optLong(var1, 0L);
    }

    public long optLong(String var1, long var2) {
        Object var4 = this.opt(var1);
        Long var5 = JSON.toLong(var4);
        return var5 != null?var5.longValue():var2;
    }

    public String getString(String var1) throws JSONException {
        Object var2 = this.get(var1);
        String var3 = JSON.toString(var2);
        if(var3 == null) {
            throw JSON.typeMismatch(var1, var2, "String");
        } else {
            return var3;
        }
    }

    public String optString(String var1) {
        return this.optString(var1, "");
    }

    public String optString(String var1, String var2) {
        Object var3 = this.opt(var1);
        String var4 = JSON.toString(var3);
        return var4 != null?var4:var2;
    }

    public JSONArray getJSONArray(String var1) throws JSONException {
        Object var2 = this.get(var1);
        if(var2 instanceof JSONArray) {
            return (JSONArray)var2;
        } else {
            throw JSON.typeMismatch(var1, var2, "JSONArray");
        }
    }

    public JSONArray optJSONArray(String var1) {
        Object var2 = this.opt(var1);
        return var2 instanceof JSONArray?(JSONArray)var2:null;
    }

    public JSONObject getJSONObject(String var1) throws JSONException {
        Object var2 = this.get(var1);
        if(var2 instanceof JSONObject) {
            return (JSONObject)var2;
        } else {
            throw JSON.typeMismatch(var1, var2, "JSONObject");
        }
    }

    public JSONObject optJSONObject(String var1) {
        Object var2 = this.opt(var1);
        return var2 instanceof JSONObject?(JSONObject)var2:null;
    }

    public JSONArray toJSONArray(JSONArray var1) throws JSONException {
        JSONArray var2 = new JSONArray();
        if(var1 == null) {
            return null;
        } else {
            int var3 = var1.length();
            if(var3 == 0) {
                return null;
            } else {
                for(int var4 = 0; var4 < var3; ++var4) {
                    String var5 = JSON.toString(var1.opt(var4));
                    var2.put(this.opt(var5));
                }

                return var2;
            }
        }
    }

    public Iterator keys() {
        return this.nameValuePairs.keySet().iterator();
    }

    public JSONArray names() {
        return this.nameValuePairs.isEmpty()?null:new JSONArray(new ArrayList(this.nameValuePairs.keySet()));
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
        var1.object();
        Iterator var2 = this.nameValuePairs.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            var1.key((String)var3.getKey()).value(var3.getValue());
        }

        var1.endObject();
    }

    public static String numberToString(Number var0) throws JSONException {
        if(var0 == null) {
            throw new JSONException("Number must be non-null");
        } else {
            double var1 = var0.doubleValue();
            JSON.checkDouble(var1);
            if(var0.equals(NEGATIVE_ZERO)) {
                return "-0";
            } else {
                long var3 = var0.longValue();
                return var1 == (double)var3?Long.toString(var3):var0.toString();
            }
        }
    }

    public static String quote(String var0) {
        if(var0 == null) {
            return "\"\"";
        } else {
            try {
                JSONStringer var1 = new JSONStringer();
                var1.open(JSONStringer.Scope.NULL, "");
                var1.value(var0);
                var1.close(JSONStringer.Scope.NULL, JSONStringer.Scope.NULL, "");
                return var1.toString();
            } catch (JSONException var2) {
                throw new AssertionError();
            }
        }
    }

    public static Object wrap(Object var0) {
        if(var0 == null) {
            return NULL;
        } else if(!(var0 instanceof JSONArray) && !(var0 instanceof JSONObject)) {
            if(var0.equals(NULL)) {
                return var0;
            } else {
                try {
                    if(var0 instanceof Collection) {
                        return new JSONArray((Collection)var0);
                    }

                    if(var0.getClass().isArray()) {
                        return new JSONArray(var0);
                    }

                    if(var0 instanceof Map) {
                        return new JSONObject((Map)var0);
                    }

                    if(var0 instanceof Boolean || var0 instanceof Byte || var0 instanceof Character || var0 instanceof Double || var0 instanceof Float || var0 instanceof Integer || var0 instanceof Long || var0 instanceof Short || var0 instanceof String) {
                        return var0;
                    }

                    if(var0.getClass().getPackage().getName().startsWith("java.")) {
                        return var0.toString();
                    }
                } catch (Exception var2) {
                    ;
                }

                return null;
            }
        } else {
            return var0;
        }
    }

}
