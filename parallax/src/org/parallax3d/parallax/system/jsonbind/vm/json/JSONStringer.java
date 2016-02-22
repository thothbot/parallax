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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSONStringer {
    final StringBuilder out = new StringBuilder();
    private final List<Scope> stack = new ArrayList();
    private final String indent;

    public JSONStringer() {
        this.indent = null;
    }

    JSONStringer(int var1) {
        char[] var2 = new char[var1];
        Arrays.fill(var2, ' ');
        this.indent = new String(var2);
    }

    public JSONStringer array() throws JSONException {
        return this.open(JSONStringer.Scope.EMPTY_ARRAY, "[");
    }

    public JSONStringer endArray() throws JSONException {
        return this.close(JSONStringer.Scope.EMPTY_ARRAY, JSONStringer.Scope.NONEMPTY_ARRAY, "]");
    }

    public JSONStringer object() throws JSONException {
        return this.open(JSONStringer.Scope.EMPTY_OBJECT, "{");
    }

    public JSONStringer endObject() throws JSONException {
        return this.close(JSONStringer.Scope.EMPTY_OBJECT, JSONStringer.Scope.NONEMPTY_OBJECT, "}");
    }

    JSONStringer open(JSONStringer.Scope var1, String var2) throws JSONException {
        if(this.stack.isEmpty() && this.out.length() > 0) {
            throw new JSONException("Nesting problem: multiple top-level roots");
        } else {
            this.beforeValue();
            this.stack.add(var1);
            this.out.append(var2);
            return this;
        }
    }

    JSONStringer close(JSONStringer.Scope var1, JSONStringer.Scope var2, String var3) throws JSONException {
        JSONStringer.Scope var4 = this.peek();
        if(var4 != var2 && var4 != var1) {
            throw new JSONException("Nesting problem");
        } else {
            this.stack.remove(this.stack.size() - 1);
            if(var4 == var2) {
                this.newline();
            }

            this.out.append(var3);
            return this;
        }
    }

    private JSONStringer.Scope peek() throws JSONException {
        if(this.stack.isEmpty()) {
            throw new JSONException("Nesting problem");
        } else {
            return (JSONStringer.Scope)this.stack.get(this.stack.size() - 1);
        }
    }

    private void replaceTop(JSONStringer.Scope var1) {
        this.stack.set(this.stack.size() - 1, var1);
    }

    public JSONStringer value(Object var1) throws JSONException {
        if(this.stack.isEmpty()) {
            throw new JSONException("Nesting problem");
        } else if(var1 instanceof JSONArray) {
            ((JSONArray)var1).writeTo(this);
            return this;
        } else if(var1 instanceof JSONObject) {
            ((JSONObject)var1).writeTo(this);
            return this;
        } else {
            this.beforeValue();
            if(var1 != null && !(var1 instanceof Boolean) && var1 != JSONObject.NULL) {
                if(var1 instanceof Number) {
                    this.out.append(JSONObject.numberToString((Number)var1));
                } else {
                    this.string(var1.toString());
                }
            } else {
                this.out.append(var1);
            }

            return this;
        }
    }

    public JSONStringer value(boolean var1) throws JSONException {
        if(this.stack.isEmpty()) {
            throw new JSONException("Nesting problem");
        } else {
            this.beforeValue();
            this.out.append(var1);
            return this;
        }
    }

    public JSONStringer value(double var1) throws JSONException {
        if(this.stack.isEmpty()) {
            throw new JSONException("Nesting problem");
        } else {
            this.beforeValue();
            this.out.append(JSONObject.numberToString(Double.valueOf(var1)));
            return this;
        }
    }

    public JSONStringer value(long var1) throws JSONException {
        if(this.stack.isEmpty()) {
            throw new JSONException("Nesting problem");
        } else {
            this.beforeValue();
            this.out.append(var1);
            return this;
        }
    }

    private void string(String var1) {
        this.out.append("\"");
        int var2 = 0;

        for(int var3 = var1.length(); var2 < var3; ++var2) {
            char var4 = var1.charAt(var2);
            switch(var4) {
                case '\b':
                    this.out.append("\\b");
                    break;
                case '\t':
                    this.out.append("\\t");
                    break;
                case '\n':
                    this.out.append("\\n");
                    break;
                case '\f':
                    this.out.append("\\f");
                    break;
                case '\r':
                    this.out.append("\\r");
                    break;
                case '\"':
                case '/':
                case '\\':
                    this.out.append('\\').append(var4);
                    break;
                default:
                    if(var4 <= 31) {
                        this.out.append(String.format("\\u%04x", new Object[]{Integer.valueOf(var4)}));
                    } else {
                        this.out.append(var4);
                    }
            }
        }

        this.out.append("\"");
    }

    private void newline() {
        if(this.indent != null) {
            this.out.append("\n");

            for(int var1 = 0; var1 < this.stack.size(); ++var1) {
                this.out.append(this.indent);
            }

        }
    }

    public JSONStringer key(String var1) throws JSONException {
        if(var1 == null) {
            throw new JSONException("Names must be non-null");
        } else {
            this.beforeKey();
            this.string(var1);
            return this;
        }
    }

    private void beforeKey() throws JSONException {
        JSONStringer.Scope var1 = this.peek();
        if(var1 == JSONStringer.Scope.NONEMPTY_OBJECT) {
            this.out.append(',');
        } else if(var1 != JSONStringer.Scope.EMPTY_OBJECT) {
            throw new JSONException("Nesting problem");
        }

        this.newline();
        this.replaceTop(JSONStringer.Scope.DANGLING_KEY);
    }

    private void beforeValue() throws JSONException {
        if(!this.stack.isEmpty()) {
            JSONStringer.Scope var1 = this.peek();
            if(var1 == JSONStringer.Scope.EMPTY_ARRAY) {
                this.replaceTop(JSONStringer.Scope.NONEMPTY_ARRAY);
                this.newline();
            } else if(var1 == JSONStringer.Scope.NONEMPTY_ARRAY) {
                this.out.append(',');
                this.newline();
            } else if(var1 == JSONStringer.Scope.DANGLING_KEY) {
                this.out.append(this.indent == null?":":": ");
                this.replaceTop(JSONStringer.Scope.NONEMPTY_OBJECT);
            } else if(var1 != JSONStringer.Scope.NULL) {
                throw new JSONException("Nesting problem");
            }

        }
    }

    public String toString() {
        return this.out.length() == 0?null:this.out.toString();
    }

    static enum Scope {
        EMPTY_ARRAY,
        NONEMPTY_ARRAY,
        EMPTY_OBJECT,
        DANGLING_KEY,
        NONEMPTY_OBJECT,
        NULL;

        private Scope() {
        }
    }

}
